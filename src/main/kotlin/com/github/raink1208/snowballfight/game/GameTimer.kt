package com.github.raink1208.snowballfight.game

import org.bukkit.scheduler.BukkitRunnable

class GameTimer(private val game: SnowballFightGame): BukkitRunnable() {
    override fun run() {
        game.teamCheck()
    }
}