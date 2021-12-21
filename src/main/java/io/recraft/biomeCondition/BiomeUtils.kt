package io.recraft.biomeCondition

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.dedicated.DedicatedServer
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.chunk.LevelChunk
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_18_R1.CraftServer
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld


object BiomeUtils {

  private var biomeMap: MutableMap<String, Int> = HashMap()

  fun getBiomeName(location: Location): Pair<String, String> {
    val key: ResourceLocation = getBiomeKey(location)
    return Pair(key.namespace, key.path)
  }

  val biomeRegistry: Registry<Biome>
    get() {
      val dedicatedServer: DedicatedServer = (Bukkit.getServer() as CraftServer).server
      return dedicatedServer.registryAccess().registry(Registry.BIOME_REGISTRY).get()
    }

  fun getBiomeKey(location: Location): ResourceLocation {
    val registry: Registry<Biome> = biomeRegistry
    val resourceKey = getBiomeBase(location)
    return registry.getKey(resourceKey)!!
  }

  fun getBiomeBase(location: Location): Biome {
    val pos = BlockPos(location.blockX, location.blockY, location.blockZ)
    val nmsChunk: LevelChunk = (location.world as CraftWorld).handle.getChunkAt(pos)
    return nmsChunk.getNoiseBiome(pos.x, 0, pos.z)
  }


  fun getBiomeAt(location: AbstractLocation): Int {
    val biome: Biome =
      (location.world as CraftWorld).handle.getBiome(BlockPos(location.blockX, location.blockY, location.blockZ))
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

