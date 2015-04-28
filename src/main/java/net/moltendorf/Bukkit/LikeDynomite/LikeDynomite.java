package net.moltendorf.Bukkit.LikeDynomite;

import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author moltendorf
 */
public class LikeDynomite extends JavaPlugin {
	protected static LikeDynomite instance;

	// Variable data.
	protected Configuration configuration = null;

	@Override
	public void onEnable() {
		instance = this;

		// Construct new configuration.
		configuration = new Configuration();

		// Are we enabled?
		if (!configuration.global.enabled) {
			return;
		}

		// Get server.
		final Server server = getServer();

		// Get plugin manager.
		final PluginManager manager = server.getPluginManager();

		// Register our event listeners.
		manager.registerEvents(new Listeners(), this);
	}

	@Override
	public void onDisable() {
		instance = null;
	}
}
