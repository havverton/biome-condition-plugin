package io.recraft.biomeCondition

import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.conditions.ILocationCondition
import io.lumine.mythic.core.skills.SkillCondition
import io.lumine.mythic.core.utils.annotations.MythicCondition
import io.lumine.mythic.core.utils.annotations.MythicField
import io.lumine.mythic.utils.logging.Log

import java.util.*


@MythicCondition(
  author = "Havverton",
  name = "biomes",
  description = "Tests if the target is within the given list of real biomes (biomes added by datapacks)"
)
class BiomeCondition(line: String?, mlc: MythicLineConfig, conditionVar: String) :
  SkillCondition(line), ILocationCondition {
  @MythicField(name = "biomes", aliases = ["b"], description = "A list of biomes to check")
  private val biomes: MutableSet<String> = HashSet()
  private val biomeIds: MutableSet<Int> = HashSet()
  private val biomeMap = BiomeUtils.getBiomeMap()

  init {
    val b = mlc.getString(arrayOf("biomes", "b"), "minecraft:plains", conditionVar)
    for (s: String in b.split(",")) {
      biomes.add(s.lowercase(Locale.getDefault()))
    }

    for (biome: String in biomes) {
      if (biomeMap.containsKey(biome)) {
        biomeIds.add(biomeMap.getValue(biome))
      } else Log.info("Could not locate biome with name \"$biome\" in file ")
    }
  }

  override fun check(abstractLocation: AbstractLocation): Boolean {
    return biomeIds.contains(
      BiomeUtils.getBiomeAt(abstractLocation)
    )
  }
}

