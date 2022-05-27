package io.recraft.biomeCondition


import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent
import io.lumine.mythic.utils.logging.Log
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin


class BiomeConditionExtension : JavaPlugin(), Listener {
  override fun onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this)
  }

  @EventHandler
  fun onMythicConditionLoad(event: MythicConditionLoadEvent) {
    Log.info("MythicConditionLoadEvent called for condition " + event.conditionName)
    if (event.conditionName.equals("biomes")) {
      val condition: BiomeCondition = BiomeCondition(event.config.line, event.config, event.container.conditionArgument)
      event.register(condition)
      Log.info("-- Registered recraftBiome condition!")
    }
  }
}
