package net.moltendorf.bukkit.likedynomite

import org.bukkit.plugin.java.JavaPlugin

/**

 * @author moltendorf
 */
class LikeDynomite : JavaPlugin() {
  lateinit var settings: Settings

  override fun onEnable() {
    instance = this

    // Construct new configuration.
    settings = Settings()

    // Are we enabled?
    enabled = settings.enabled

    if (enabled) {
      // Get plugin manager.
      val manager = server.pluginManager

      // Register listeners.
      manager.registerEvents(Listeners(), this)
    }
  }

  override fun onDisable() {
    enabled = false
  }

  companion object {
    var enabled = false
      private set

    lateinit var instance: LikeDynomite
      private set
  }
}
