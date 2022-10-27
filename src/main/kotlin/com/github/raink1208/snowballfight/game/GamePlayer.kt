package com.github.raink1208.snowballfight.game

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GamePlayer(val player: Player): Audience {
    var status = PlayerStatus.GAME_READY
    var team: GameTeam? = null
    var health = 0; private set

    fun initPlayer() {
        if (team == null) status = PlayerStatus.SPECTATOR
        if (status == PlayerStatus.SPECTATOR) {
            player.gameMode = GameMode.SPECTATOR
            return
        }
        player.inventory.clear()
        for (activePotionEffect in player.activePotionEffects) {
            player.removePotionEffect(activePotionEffect.type)
        }
        player.inventory.addItem(ItemStack(Material.SNOWBALL, 3))
        player.health = 20.0
        player.foodLevel = 20

        player.teleport(team?.spawnLocation!!)

        player.gameMode = GameMode.ADVENTURE
        status = PlayerStatus.IN_GAME
    }

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