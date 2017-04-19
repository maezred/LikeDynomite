package net.moltendorf.bukkit.likedynomite

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.EntityType
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import java.util.ArrayList
import java.util.Collections

/**
 * Listener register.

 * @author moltendorf
 */
class Listeners : Listener {
  private val chain = ExplosionChain()

  @EventHandler(priority = EventPriority.HIGHEST)
  fun PlayerInteractEventHandler(event: PlayerInteractEvent) {
    val block = event.clickedBlock

    // Did the player right-click TNT?
    if (block != null && block.type == Material.TNT) {
      val item = event.item

      // Did they use Flint And Steel?
      if (item != null && item.type == Material.FLINT_AND_STEEL) {
        chain.add(block) // Queue it up.

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

    // We were just pretending.
    event.isCancelled = false

    // Get all the blocks that will be destroyed.
    val iterator = event.blockList().iterator()
    val tnt = ArrayList<Block>()

    while (iterator.hasNext()) {
      val block = iterator.next()

      // Is this a block?
      if (block !== null) {
        iterator.remove()

        // Only TNT goes boom.
        if (block.type === Material.TNT && tnt.size + chain.size < settings.max) {
          tnt.add(block) // Queue it up.
        }
      }
    }

    Collections.shuffle(tnt)
    chain.addAll(tnt)
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  fun HangingBreakEventHandler(event: HangingBreakEvent) {
    if (event.cause === HangingBreakEvent.RemoveCause.EXPLOSION && !settings.damageable.contains(event.entity.type)) {
      event.isCancelled = true
    }
  }
}
