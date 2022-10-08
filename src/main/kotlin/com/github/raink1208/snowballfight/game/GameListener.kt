package com.github.raink1208.snowballfight.game

import net.kyori.adventure.text.Component
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.util.Vector

class GameListener(private val game: SnowballFightGame): Listener {
    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        if (event.entity !is Snowball) return
        if (game.gameStatus != SnowballFightGame.GameStatus.IN_GAME) return
        val launchPlayer = event.entity.shooter
        val hitPlayer = event.hitEntity
        if (launchPlayer !is Player || hitPlayer !is Player) return
        val launchGamePlayer = game.getGamePlayer(launchPlayer)
        val hitGamePlayer = game.getGamePlayer(hitPlayer)
        if (launchGamePlayer != null && hitGamePlayer != null) {
            if (launchGamePlayer.team == null || hitGamePlayer.team == null) return
            if (launchGamePlayer.team == hitGamePlayer.team) return
            hitPlayer.velocity = Vector.getRandom()
            hitPlayer.velocity.multiply(hitGamePlayer.health)
            hitGamePlayer.damage()
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val gamePlayer = game.getGamePlayer(event.player) ?: return
        gamePlayer.status = GamePlayer.PlayerStatus.SPECTATOR
        game.broadcastMessage(event.player.name + "が倒された")
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val gamePlayer = game.getGamePlayer(event.player)
        gamePlayer?.status = GamePlayer.PlayerStatus.SPECTATOR
        gamePlayer?.team?.sendMessage(Component.text(event.player.name + "は退出しました"))
    }
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val gamePlayer = game.getGamePlayer(event.player)
        if (gamePlayer != null) {
            event.player.sendMessage("ゲーム中に退出したので観戦モードになりました")
            event.player.gameMode = GameMode.SPECTATOR
        }
    }
}