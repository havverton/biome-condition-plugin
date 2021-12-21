//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package io.recraft.mysticmobsextension

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillCondition
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition
import io.lumine.xikage.mythicmobs.util.annotations.MythicCondition
import io.lumine.xikage.mythicmobs.util.annotations.MythicField
import io.lumine.xikage.mythicmobs.utils.logging.Log
import org.bukkit.Location
import java.util.*


@MythicCondition(
  author = "Havverton",
  name = "recraftbiome",
  description = "Tests if the target is within the given list of real biomes (biomes added by datapacks)"
)
class BiomeCondition(line: String?, mlc: MythicLineConfig, conditionVar: String) :
  SkillCondition(line), ILocationCondition {
  @MythicField(name = "recraftbiome", aliases = ["rb"], description = "A list of biomes to check")
  private val biomes: MutableSet<String> = HashSet()
  private val biomeIds: Set<Int> = HashSet()

  init {
    var craftBiome : String = "";
    val b = mlc.getString(arrayOf("recraftbiome", "rb"), "plains", *arrayOf(conditionVar))
    for (s: String in b.split(",".toRegex()).toTypedArray()) {
      craftBiome = if (!s.contains(":")) {
        "minecraft:$s"
      }else s
      biomes.add(craftBiome.lowercase(Locale.getDefault()))
    }
    val biomeIds: MutableSet<Int> = HashSet()
    for (biome: String in biomes) {
      val formattedKey = NMSBiomeUtils.getBiomeID(biome)
      if (NMSBiomeUtils.getBiomeMap().containsKey(formattedKey)) {
        NMSBiomeUtils.getBiomeMap().get(biome)?.let { biomeIds.add(it) }
      }
      else Log.info("Could not locate biome with name \"$biome\" in file ")
    }
  }

  override fun check(abstractLocation: AbstractLocation): Boolean {
    return biomeIds.contains(
      NMSBiomeUtils.getBiomeAt(abstractLocation)
    )
  }
}

