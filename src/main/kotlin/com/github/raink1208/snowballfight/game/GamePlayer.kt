package com.github.raink1208.snowballfight.game

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart
import org.bukkit.entity.Player

class GamePlayer(val player: Player): Audience {
    var status = PlayerStatus.SPECTATOR; private set
    lateinit var team: GameTeam
    val health = 0

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
        SPECTATOR,
        IN_GAME
    }
}