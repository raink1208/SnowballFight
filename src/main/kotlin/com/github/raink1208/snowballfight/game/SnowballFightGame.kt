package com.github.raink1208.snowballfight.game

import com.github.raink1208.snowballfight.Main
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import java.util.*
import kotlin.collections.HashSet


class SnowballFightGame(val map: GameMap) {
    var gameStatus = GameStatus.BEFORE_GAME; private set
    private val gameEventListener = GameListener(this)
    private val gameTimer = GameTimer(this)
    val players = mutableMapOf<UUID,GamePlayer>()
    val teams = HashSet<GameTeam>()

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
        gameTimer.runTaskTimer(Main.instance, 0, 20)
    }

    fun end() {
        broadcastMessage("ゲームを終了します")
        gameStatus = GameStatus.AFTER_GAME
        HandlerList.unregisterAll(gameEventListener)
        gameTimer.cancel()
    }

    fun teamCheck() {
        val surviveTeam = teams.filter { it.isDemolition() }
        if (surviveTeam.size == 1) end()
    }

    fun joinPlayer(player: Player) {
        val gamePlayer = GamePlayer(player)
        players[player.uniqueId] = gamePlayer
        broadcastMessage(player.name + "が参加しました")
    }

    fun leavePlayer(gamePlayer: GamePlayer) {
        gamePlayer.team?.leaveTeam(gamePlayer)
        players.remove(gamePlayer.player.uniqueId)
        broadcastMessage(gamePlayer.player.name + "が退出しました")
    }

    fun getGamePlayer(player: Player): GamePlayer? {
        return players[player.uniqueId]
    }

    private fun createTeam() {
        for ((i, spawn) in map.spawnLocations.withIndex()) {
            val name = "team-" + (i+1)
            val team = GameTeam(this, name, spawn)
            teams.add(team)
        }
    }

    fun getTeam(name: String): GameTeam? {
        return teams.find { it.teamName == name }
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