package com.github.raink1208.snowballfight.game

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.Component

class GameTeam(val teamName: String): ForwardingAudience {
    val member = mutableListOf<GamePlayer>()

    fun joinTeam(gamePlayer: GamePlayer) {
        sendMessage(Component.text(gamePlayer.player.name + "がチームに参加しました"))
        member.add(gamePlayer)
        gamePlayer.team = this
    }

    override fun audiences(): MutableIterable<Audience> {
        return member
    }
}