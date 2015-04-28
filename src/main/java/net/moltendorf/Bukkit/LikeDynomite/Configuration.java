package net.moltendorf.Bukkit.LikeDynomite;

import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Configuration class.
 *
 * @author moltendorf
 */
public class Configuration {

	static protected class Global {

		// Final data.
		final protected boolean enabled = true; // Whether or not the plugin is enabled at all; useful for using it as an interface (default is true).

		final protected HashSet<EntityType> damageable = new HashSet<>(Arrays.asList(
			EntityType.BAT,
			EntityType.BLAZE,
			EntityType.CAVE_SPIDER,
			EntityType.CHICKEN,
			EntityType.COW,
			EntityType.CREEPER,
			EntityType.ENDERMAN,
			EntityType.ENDER_DRAGON,
			EntityType.GHAST,
			EntityType.GIANT,
			EntityType.IRON_GOLEM,
			EntityType.MAGMA_CUBE,
			EntityType.MINECART_TNT,
			EntityType.MUSHROOM_COW,
			EntityType.PIG,
			EntityType.PIG_ZOMBIE,
			EntityType.PLAYER,
			EntityType.SHEEP,
			EntityType.SILVERFISH,
			EntityType.SKELETON,
			EntityType.SLIME,
			EntityType.SNOWMAN,
			EntityType.SPIDER,
			EntityType.SQUID,
			EntityType.WITCH,
			EntityType.WITHER,
			EntityType.ZOMBIE
		));

		final protected int max = 1200;

	}

	// Final data.
	final protected Global global = new Global();

	public Configuration() {

		// Placeholder.
	}
}
