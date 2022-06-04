package io.recraft.biomeCondition

import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.conditions.ILocationCondition
import io.lumine.mythic.core.skills.SkillCondition
import io.lumine.mythic.core.utils.annotations.MythicCondition
import io.lumine.mythic.core.utils.annotations.MythicField
import io.recraft.biomeCondition.BiomeConditionPlugin.Companion.plugin


@MythicCondition(
  author = "Havverton",
  name = "biomes",
  description = "Tests if the target is within the given list of real biomes (biomes added by datapacks)"
)
class BiomeCondition(line: String?, mlc: MythicLineConfig, conditionVar: String) :
  SkillCondition(line), ILocationCondition {
  @MythicField(name = "biomes", aliases = ["b"], description = "A list of biomes to check")
  private val biomes: MutableSet<String> = HashSet()

  init {
    val b = mlc.getString(arrayOf("biomes", "b"), "minecraft:plains", conditionVar)
    for (s: String in b.split(",")) {
      biomes.add(s.lowercase().trim())
    }

    for (biome: String in biomes) {
      if (BiomeUtils.biomes.contains(biome)) {
        biomes.add(biome)
      } else {
        plugin.logger.warning("Could not locate biome with name \"$biome\", line ${mlc.line}")
      }
    }
  }

  override fun check(abstractLocation: AbstractLocation): Boolean {
    return biomes.contains(
      BiomeUtils.getBiomeAt(abstractLocation)
    )
  }
}
