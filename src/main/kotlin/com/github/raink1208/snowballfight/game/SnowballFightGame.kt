package com.github.raink1208.snowballfight.game

import com.github.raink1208.snowballfight.Main
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import java.util.*


class SnowballFightGame {
    var gameStatus = GameStatus.BEFORE_GAME; private set
    private val gameEventListener = GameListener(this)
    private val players = mutableMapOf<UUID,GamePlayer>()
    private val teams = mutableMapOf<String, GameTeam>()

    fun start() {
        broadcastMessage("ゲームを開始します")
        gameStatus = GameStatus.IN_GAME
        Main.instance.server.pluginManager.registerEvents(gameEventListener, Main.instance)
    }

    fun end() {
        HandlerList.unregisterAll(gameEventListener)
    }

    fun stop() {

    }

    fun joinPlayer(player: Player) {
        val gamePlayer = GamePlayer(player)
        players[player.uniqueId] = gamePlayer
        broadcastMessage(player.name + "が参加しました")
    }

    fun getGamePlayer(player: Player): GamePlayer? {
        return players[player.uniqueId]
    }

    fun createTeam(name: String): Boolean {
        if (teams.containsKey(name)) return false
        val team = GameTeam(name)
        teams[name] = team
        return true
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