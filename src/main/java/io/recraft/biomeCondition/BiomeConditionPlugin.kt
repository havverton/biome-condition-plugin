package io.recraft.biomeCondition

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class BiomeConditionPlugin : JavaPlugin(), Listener {

  companion object {
    lateinit var plugin: JavaPlugin
      private set
  }

  override fun onLoad() {
    plugin = this
  }

  override fun onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this)
  }

  @EventHandler
  fun onMythicConditionLoad(event: MythicConditionLoadEvent) {
    if (event.conditionName.equals("biomes")) {
      val condition = BiomeCondition(event.config.line, event.config, event.container.conditionArgument)
      event.register(condition)
    }
  }
}
