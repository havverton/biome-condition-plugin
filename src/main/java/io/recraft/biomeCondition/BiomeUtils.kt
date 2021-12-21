package io.recraft.biomeCondition

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.server.dedicated.DedicatedServer
import net.minecraft.world.level.biome.Biome
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.craftbukkit.v1_18_R1.CraftServer
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld


object BiomeUtils {

  private var biomeMap: MutableMap<String, Int> = HashMap()

  private val biomeRegistry: Registry<Biome>
    get() {
      val dedicatedServer: DedicatedServer = (Bukkit.getServer() as CraftServer).server
      return dedicatedServer.registryAccess().registry(Registry.BIOME_REGISTRY).get()
    }

  fun getBiomeAt(location: AbstractLocation): Int {
    val dedicatedServer: DedicatedServer = (Bukkit.getServer() as CraftServer).server
    val world: World? = dedicatedServer.server.getWorld(location.world.uniqueId)
    val biome: Biome =
      (world as CraftWorld).handle.getBiome(BlockPos(location.blockX, location.blockY, location.blockZ))
    return biomeRegistry.getId(biome)
  }


  fun getBiomeMap(): MutableMap<String, Int> {
    if (biomeMap.isEmpty()) {
      biomeRegistry.forEach { entry ->
        val name: String = biomeRegistry.getKey(entry).toString()
        val id: Int = biomeRegistry.getId(entry)
        biomeMap[name] = id
      }
    }
    return biomeMap
  }
}

