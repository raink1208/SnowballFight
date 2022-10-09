package com.github.raink1208.snowballfight.game

import net.kyori.adventure.text.Component
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
        if (!inGame()) return
        val snowball = event.entity as? Snowball ?: return
        val launchPlayer = snowball.shooter as? Player ?: return
        val hitPlayer = event.hitEntity as? Player ?: return
        val launchGamePlayer = game.getGamePlayer(launchPlayer) ?: return
        val hitGamePlayer = game.getGamePlayer(hitPlayer) ?: return

        if (launchGamePlayer.team != null && hitGamePlayer.team != null) return
        if (launchGamePlayer.team == hitGamePlayer.team) return
        val velocity = snowball.velocity.normalize()
        hitPlayer.velocity = Vector(velocity.x, 1.0, velocity.z)
        hitPlayer.velocity.multiply(hitGamePlayer.health)
        hitGamePlayer.damage()
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        if (!inGame()) return
        val gamePlayer = game.getGamePlayer(event.player) ?: return
        gamePlayer.status = GamePlayer.PlayerStatus.SPECTATOR
        game.broadcastMessage(event.player.name + "が倒された")
        game.update()
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val gamePlayer = game.getGamePlayer(event.player) ?: return
        gamePlayer.status = GamePlayer.PlayerStatus.SPECTATOR
        gamePlayer.team?.sendMessage(Component.text(event.player.name + "が観戦モードになっています"))
        game.update()
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val gamePlayer = game.getGamePlayer(event.player) ?: return
        if (game.gameStatus == SnowballFightGame.GameStatus.BEFORE_GAME) {
            gamePlayer.status = GamePlayer.PlayerStatus.GAME_READY
            return
        }
        event.player.sendMessage("サーバーから退出したため観戦モードになっています")
    }

    private fun inGame(): Boolean {
        return game.gameStatus == SnowballFightGame.GameStatus.IN_GAME
    }
}