package com.github.raink1208.snowballfight.game

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.Component

class GameTeam(val teamName: String): ForwardingAudience {
    val member = mutableListOf<GamePlayer>()

    fun joinTeam(gamePlayer: GamePlayer) {
        if (gamePlayer.team != null) return
        member.add(gamePlayer)
        sendMessage(Component.text(gamePlayer.player.name + "がチームに参加しました"))
        gamePlayer.team = this
    }

    fun leaveTeam(gamePlayer: GamePlayer) {
        sendMessage(Component.text(gamePlayer.player.name + "がチームから退出しました"))
        member.remove(gamePlayer)
        gamePlayer.team = null
    }

    fun isDemolition(): Boolean {
        for (i in member) {
            if (i.status == GamePlayer.PlayerStatus.IN_GAME)
                return false
        }
        return true
    }

    override fun audiences(): MutableIterable<Audience> {
        return member
    }
}