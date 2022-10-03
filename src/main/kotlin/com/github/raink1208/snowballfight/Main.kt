package com.github.raink1208.snowballfight

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin(), Listener {
    companion object {
        lateinit var instance: Main
        private set
    }

    @EventHandler
    fun onHit(event: ProjectileHitEvent) {
        val hit = event.hitEntity as? Player ?: return
        hit.velocity = event.entity.velocity
        println(event.entity.velocity)
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
        instance = this
    }
}