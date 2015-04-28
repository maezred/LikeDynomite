package net.moltendorf.Bukkit.LikeDynomite;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

/**
 * Listener register.
 *
 * @author moltendorf
 */
public class Listeners implements Listener {

	@EventHandler()
	public void PlayerInteractEventHandler(final PlayerInteractEvent event) {
		final Block block = event.getClickedBlock();

		if (block != null && block.getType() == Material.TNT) {
			final ItemStack item = event.getPlayer().getItemInHand();

			if (item != null && item.getType() == Material.FLINT_AND_STEEL) {
				final Location location = block.getLocation().add(0.5, 0, 0.5);
				final World world = location.getWorld();

				block.setType(Material.AIR);
				world.playSound(location, Sound.FUSE, 1, 1);
				world.spawnEntity(location, EntityType.PRIMED_TNT);

				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void EntityExplodeEventLowestHandler(final EntityExplodeEvent event) {
		if (event.getEntityType() == EntityType.PRIMED_TNT) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void EntityExplodeEventHighestHandler(final EntityExplodeEvent event) {
		if (event.getEntityType() == EntityType.PRIMED_TNT) {
			final TNTPrimed tnt = (TNTPrimed)event.getEntity();
			final Entity entity = tnt.getSource();

			if (entity == null) {
				event.setCancelled(false);

				for (final Iterator<Block> iterator = event.blockList().iterator(); iterator.hasNext(); ) {
					final Block block = iterator.next();

					if (block != null && block.getType() != Material.TNT) {
						iterator.remove();
					}
				}
			} else {
				final World world = event.getLocation().getWorld();

				for (final Iterator<Block> iterator = event.blockList().iterator(); iterator.hasNext(); ) {
					final Block block = iterator.next();

					if (block != null) {
						if (block.getType() == Material.TNT) {
							block.setType(Material.AIR);

							world.spawnEntity(block.getLocation(), EntityType.PRIMED_TNT);
						} else {
							iterator.remove();
						}
					}
				}

				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void HangingBreakEventHandler(final HangingBreakEvent event) {
		if (event.getCause() == HangingBreakEvent.RemoveCause.EXPLOSION) {
			if (LikeDynomite.instance.configuration.global.damageable.contains(event.getEntity().getType())) {
				event.setCancelled(false);
			} else {
				event.setCancelled(true);
			}
		}
	}
}
