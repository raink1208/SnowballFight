package com.github.raink1208.snowballfight.util

import com.github.raink1208.snowballfight.Main
import com.github.raink1208.snowballfight.game.GameMap
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.configuration.serialization.ConfigurationSerialization
import java.io.File

object GameMapConfig {
    private const val name = "maps.yml"
    private val file = File(Main.instance.dataFolder, name)
    private val config: YamlConfiguration

    init {
        ConfigurationSerialization.registerClass(GameMap::class.java, "GameMap")
        config = YamlConfiguration.loadConfiguration(file)
    }

    fun getGameMap(mapName: String): GameMap? {
        if (!config.contains(mapName)) return null
        return config.getObject(mapName, GameMap::class.java)
    }

    fun save(map: GameMap) {
        config.set(map.mapName, map)
        config.save(file)
    }

    fun saveDefaultConfig() {
        if (!Main.instance.dataFolder.exists()) {
            Main.instance.dataFolder.mkdir()
        }
        if (!file.exists()) {
            file.createNewFile()
        }
    }
}