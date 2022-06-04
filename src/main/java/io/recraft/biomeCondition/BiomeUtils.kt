package io.recraft.biomeCondition

import io.lumine.mythic.api.adapters.AbstractLocation
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.server.dedicated.DedicatedServer
import net.minecraft.world.level.biome.Biome
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_18_R2.CraftServer
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld


object BiomeUtils {

  private val cache = mutableMapOf<Biome, String>()
  val biomes by lazy {
    // Lazy to only compute this once it's requested assuming world and data-packs are already loaded at this point
    val biomeRegistry = getBiomeRegistry()
    biomeRegistry.map { biomeRegistry.getKey(it).toString() }.toSet()
  }

  private fun getBiomeRegistry(): Registry<Biome> {
    val dedicatedServer: DedicatedServer = (Bukkit.getServer() as CraftServer).server
    return dedicatedServer.registryAccess().registry(Registry.BIOME_REGISTRY).get()
  }

  fun getBiomeAt(location: AbstractLocation): String {
    val world = Bukkit.getWorld(location.world.uniqueId)!! as CraftWorld
    val biome = world.handle.getBiome(BlockPos(location.blockX, location.blockY, location.blockZ)).value()

    return cache.computeIfAbsent(biome) { getBiomeRegistry().getKey(biome).toString() }
  }

}
