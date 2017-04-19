package net.moltendorf.bukkit.likedynomite

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.player.PlayerInteractEvent

/**
 * Listener register.

 * @author moltendorf
 */
class Listeners : Listener {
  private var disabled = false
  private var tnt = 0

  init {
    for (world in server.worlds) {
      tnt += world.getEntitiesByClass(TNTPrimed::class.java).size
    }

    // Logging task.
    scheduler.runTaskTimer(instance, {
      if (tnt >= settings.max - 25) {
        if (!disabled) {
          broadcast("${ChatColor.DARK_RED}Primed TNT is capped.")

          disabled = true
        }

        console("${ChatColor.DARK_RED}Primed TNT: $tnt.")
      } else if (tnt < settings.max - 25) {
        if (disabled) {
          broadcast("${ChatColor.DARK_GREEN}Primed TNT is no longer capped.")

          disabled = false
        }
      }
    }, 1L, 10 * 20L)
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  fun PlayerInteractEventHandler(event: PlayerInteractEvent) {
    val block = event.clickedBlock

    // Did the player right-click TNT?
    if (block != null && block.type == Material.TNT) {
      val item = event.item

      // Did they use Flint And Steel?
      if (item != null && item.type == Material.FLINT_AND_STEEL) {
        val location = block.location.add(0.5, 0.0, 0.5)
        val world = location.world

        block.type = Material.AIR // Remove the TNT from the world.

        world.playSound(location, Sound.ENTITY_TNT_PRIMED, 1f, 1f)
        world.spawnEntity(location, EntityType.PRIMED_TNT) // Bypass protections by manually spawning a primed TNT.

        // Cancel the event so another one isn't spawned (when no protection is present).
        event.isCancelled = true
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  fun EntityExplodeEventLowestPriorityHandler(event: EntityExplodeEvent) {
    // Is it TNT going boom?
    if (event.entityType === EntityType.PRIMED_TNT) {
      // Pretend to cancel the event so that it isn't blocked by a protection plugin.
      event.isCancelled = true
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  fun EntityExplodeEventHighestPriorityHandler(event: EntityExplodeEvent) {
    // Is it TNT going boom?
    if (event.entityType !== EntityType.PRIMED_TNT) {
      return
    }

    val entity = event.entity as? TNTPrimed ?: return // Can't hurt to double check.

    // Decrease our live TNT counter.
    if (tnt > 0) {
      --tnt
    }

    // Who set this off?
    val source = entity.source

    if (source === null) { // Probably another TNT.
      event.isCancelled = false

      val iterator = event.blockList().iterator()

      while (iterator.hasNext()) {
        val block = iterator.next()

        if (block !== null) {
          if (block.type === Material.TNT && tnt < settings.max) {
            ++tnt
          } else {
            iterator.remove()
          }
        }
      }
    } else {
      val world = event.location.world

      val iterator = event.blockList().iterator()

      while (iterator.hasNext()) {
        val block = iterator.next()

        if (block !== null) {
          if (block.type === Material.TNT && tnt < settings.max) {
            ++tnt

            block.type = Material.AIR

            world.spawnEntity(block.location, EntityType.PRIMED_TNT)
          } else {
            iterator.remove()
          }
        }
      }

      event.isCancelled = true
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  fun HangingBreakEventHandler(event: HangingBreakEvent) {
    if (event.cause === HangingBreakEvent.RemoveCause.EXPLOSION) {
      event.isCancelled = !settings.damageable.contains(event.entity.type)
    }
  }
}
