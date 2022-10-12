package com.github.raink1208.snowballfight.game

import com.github.raink1208.snowballfight.Main
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import java.util.*


class SnowballFightGame(val map: GameMap) {
    var gameStatus = GameStatus.BEFORE_GAME; private set
    private val gameEventListener = GameListener(this)
    val players = mutableMapOf<UUID,GamePlayer>()
    val teams = mutableMapOf<String, GameTeam>()

    init {
        Main.instance.server.pluginManager.registerEvents(gameEventListener, Main.instance)
        createTeam()
    }

    fun start() {
        broadcastMessage("ゲームを開始します")
        gameStatus = GameStatus.IN_GAME

        for ((_, player) in players) {
            player.initPlayer()
        }
    }

    fun update() {
        teamCheck()
    }

    private fun teamCheck() {
        val surviveTeam = teams.filter { it.value.isDemolition() }
        if (surviveTeam.size == 1) end()
    }

    fun end() {
        broadcastMessage("ゲームを終了します")
        gameStatus = GameStatus.AFTER_GAME
        HandlerList.unregisterAll(gameEventListener)
    }

    fun joinPlayer(player: Player) {
        val gamePlayer = GamePlayer(player)
        players[player.uniqueId] = gamePlayer
        broadcastMessage(player.name + "が参加しました")
    }

    fun leavePlayer(gamePlayer: GamePlayer) {
        val team = gamePlayer.team
        if (team != null) {
            team.leaveTeam(gamePlayer)
            if (team.member.isEmpty()) {
                deleteTeam(team.teamName)
            }
        }
        players.remove(gamePlayer.player.uniqueId)
        broadcastMessage(gamePlayer.player.name + "が退出しました")
    }

    fun getGamePlayer(player: Player): GamePlayer? {
        return players[player.uniqueId]
    }

    private fun createTeam() {
        for ((i, spawn) in map.spawnLocations.withIndex()) {
            val name = "team-" + (i+1)
            val team = GameTeam(name, spawn)
            teams[name] = team
        }
    }

    fun deleteTeam(name: String) {
        teams.remove(name)
        broadcastMessage("チーム: " + name + "が削除されました")
    }

    fun getTeam(name: String): GameTeam? {
        return teams[name]
    }

    fun broadcastMessage(msg: String) {
        Audience.audience(players.values).sendMessage(Component.text(msg))
    }

    enum class GameStatus {
        BEFORE_GAME,
        IN_GAME,
        AFTER_GAME
    }
}