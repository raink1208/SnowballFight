package com.github.raink1208.snowballfight.game

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import java.util.*


class SnowballFightGame {
    var gameStatus = GameStatus.BEFORE_GAME; private set
    private val gameEventListener = GameListener(this)
    private val players = mutableMapOf<UUID,GamePlayer>()
    private val teams = mutableListOf<GameTeam>()

    fun start() {
        broadcastMessage("ゲームを開始します")
        gameStatus = GameStatus.IN_GAME
    }

    fun joinPlayer(player: Player) {
        val gamePlayer = GamePlayer(player)
        players[player.uniqueId] = gamePlayer
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