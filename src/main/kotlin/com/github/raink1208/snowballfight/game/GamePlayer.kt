package com.github.raink1208.snowballfight.game

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart
import org.bukkit.entity.Player

class GamePlayer(val player: Player): Audience {
    var status = PlayerStatus.GAME_READY
    var team: GameTeam? = null
    var health = 0; private set

    fun damage() {
        health++
    }

    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        player.sendMessage(source, message, type)
    }

    override fun <T : Any> sendTitlePart(part: TitlePart<T>, value: T) {
        player.sendTitlePart(part, value)
    }

    override fun clearTitle() {
        player.clearTitle()
    }

    override fun resetTitle() {
        player.resetTitle()
    }

    enum class PlayerStatus {
        GAME_READY,
        SPECTATOR,
        IN_GAME
    }
}