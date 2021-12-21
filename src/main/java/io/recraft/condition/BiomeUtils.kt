
package io.recraft.mysticmobsextension

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


object NMSBiomeUtils {
   // private val BIOME_REGISTRY_RESOURCE_KEY: ResourceKey<Registry<Biome>> = Registry.BIOME_REGISTRY

  private var biomeMap: MutableMap<String, Int> = HashMap()

    fun getBiomeName(location: Location): Pair<String, String> {
        val key: ResourceLocation = getBiomeKey(location)
        return Pair(key.getNamespace(), key.getPath())
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
        return  nmsChunk.getNoiseBiome(pos.getX(), 0, pos.getZ())
    }


  fun getBiomeAt(location: AbstractLocation): Int {
    val biome: Biome =
      (location.world as CraftWorld).handle.getBiome(BlockPos(location.blockX, location.blockY, location.blockZ))
    return biomeRegistry.getId(biome)
  }


  fun getBiomeMap(): MutableMap<String, Int> {
    if (biomeMap.isEmpty()) {
     biomeRegistry.forEach { entry ->
        val name: String = entry.biomeCategory.name
        val id: Int = biomeRegistry.getId(entry)
       biomeMap[name] = id
      }
    }
    return biomeMap
  }
  fun getBiomeID(biome:String):String{
    var key:String = biome.replaceBefore(":","")
    return key.replace(":","").uppercase()
  }
}

