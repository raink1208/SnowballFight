package com.github.raink1208.snowballfight.game

import com.github.raink1208.snowballfight.Main
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.serialization.ConfigurationSerializable

class GameMap(val mapName: String, val world: World, val spawnLocations: List<Location>): ConfigurationSerializable {
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): GameMap {
            assertKey(map, "mapName")
            assertKey(map, "world")
            assertKey(map, "spawnLocations")

            val mapName = map["mapName"] as String
            val worldName = map["world"] as String
            val world = Main.instance.server.getWorld(worldName)
            val spawns = map["spawnLocations"] as List<*>
            require(world != null) { worldName + "is not found" }
            val locations = mutableListOf<Location>()
            for (loc in spawns) {
                locations.add(loc as Location)
            }
            return GameMap(mapName, world, locations)
        }

        private fun assertKey(args: Map<String, Any>, key: String) {
            require(args.containsKey(key)) { "$key is null" }
        }
    }
    override fun serialize(): MutableMap<String, Any> {
        val serialized = HashMap<String, Any>()
        serialized["mapName"] = mapName
        serialized["world"] = world.name
        serialized["spawnLocations"] = spawnLocations
        return serialized
    }
}