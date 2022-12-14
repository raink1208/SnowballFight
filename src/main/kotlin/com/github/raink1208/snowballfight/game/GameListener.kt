package com.github.raink1208.snowballfight.game

import com.github.raink1208.snowballfight.Main
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class GameListener(private val game: SnowballFightGame): Listener {
    @EventHandler
    fun onProjectileLaunch(event: ProjectileLaunchEvent) {
        if (!inGame()) return
        if (event.entity !is Snowball) return
        val player = event.entity.shooter as? Player ?: return
        if (game.getGamePlayer(player) == null) return
        Main.instance.server.scheduler.runTaskLater(Main.instance,
            Runnable {
                player.inventory.addItem(ItemStack(Material.SNOWBALL))
            }, 200)
    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        if (!inGame()) return
        val snowball = event.entity as? Snowball ?: return
        val launchPlayer = snowball.shooter as? Player ?: return
        val hitPlayer = event.hitEntity as? Player ?: return
        val launchGamePlayer = game.getGamePlayer(launchPlayer) ?: return
        val hitGamePlayer = game.getGamePlayer(hitPlayer) ?: return

        if (launchGamePlayer.team == null && hitGamePlayer.team == null) return
        if (launchGamePlayer.team == hitGamePlayer.team) return
        val velocity = snowball.velocity.normalize()
        val kb = Vector(velocity.x, .5, velocity.z).multiply(hitGamePlayer.health.toDouble() / 5.0 + 1.0)
        hitPlayer.velocity = kb
        hitGamePlayer.damage()
    }

    @EventHandler
    fun onFall(event: EntityDamageEvent) {
        if (!inGame()) return
        val player = event.entity as? Player ?: return
        if (game.getGamePlayer(player) == null) return
        if (event.cause == EntityDamageEvent.DamageCause.FALL)
            event.isCancelled = true
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        if (!inGame()) return
        val gamePlayer = game.getGamePlayer(event.player) ?: return
        gamePlayer.status = GamePlayer.PlayerStatus.SPECTATOR
        game.broadcastMessage(event.player.name + "???????????????")
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val gamePlayer = game.getGamePlayer(event.player) ?: return
        gamePlayer.status = GamePlayer.PlayerStatus.SPECTATOR
        gamePlayer.team?.sendMessage(Component.text(event.player.name + "???????????????????????????????????????"))
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val gamePlayer = game.getGamePlayer(event.player) ?: return
        if (game.gameStatus == SnowballFightGame.GameStatus.BEFORE_GAME) {
            gamePlayer.status = GamePlayer.PlayerStatus.GAME_READY
            return
        }
        event.player.sendMessage("????????????????????????????????????????????????????????????????????????")
    }

    private fun inGame(): Boolean {
        return game.gameStatus == SnowballFightGame.GameStatus.IN_GAME
    }
}