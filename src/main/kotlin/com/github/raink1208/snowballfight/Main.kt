package com.github.raink1208.snowballfight

import com.github.raink1208.snowballfight.commands.GameCommand
import com.github.raink1208.snowballfight.commands.SnowballFightCommand
import com.github.raink1208.snowballfight.game.GameMap
import com.github.raink1208.snowballfight.game.SnowballFightGame
import com.github.raink1208.snowballfight.util.GameMapConfig
import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {
    companion object {
        lateinit var instance: Main
        private set
    }

    var game: SnowballFightGame? = null; private set

    fun createGame(map: GameMap): Boolean {
        if (game == null) {
            game = SnowballFightGame(map)
            return true
        }
        return false
    }

    fun gameStart() {
        game?.start()
    }

    fun destroyGame() {
        game?.end()
        game = null
    }

    override fun onEnable() {
        instance = this

        GameMapConfig.saveDefaultConfig()

        getCommand("snowballfight")?.run {
            val command = SnowballFightCommand()
            setExecutor(command)
            tabCompleter = command
        }
        getCommand("snowballfightgame")?.run {
            val command = GameCommand()
            setExecutor(command)
        }
    }
}