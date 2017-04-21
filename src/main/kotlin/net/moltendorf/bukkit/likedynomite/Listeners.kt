package net.moltendorf.bukkit.likedynomite

import org.bukkit.Material
import org.bukkit.Material.FLINT_AND_STEEL
import org.bukkit.Material.TNT
import org.bukkit.block.Block
import org.bukkit.entity.EntityType
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.EventPriority.HIGHEST
import org.bukkit.event.EventPriority.LOWEST
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
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

  @EventHandler(priority = LOWEST)
  fun PlayerInteractEventLowestPriorityHandler(event: PlayerInteractEvent) {
    if ((event.hasItem() && event.item.type == TNT) || (event.hasBlock() && event.clickedBlock.type == TNT)) {
      // Pretend to cancel the event so that it isn't blocked by a protection plugin.
      event.isCancelled = true

      return
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  fun PlayerInteractEventHighestPriorityHandler(event: PlayerInteractEvent) {
    // Is the player placing TNT?
    when (event.item?.type) {
      TNT -> {
        // We were just pretending.
        event.isCancelled = false

        return
      }

      FLINT_AND_STEEL -> { // Did the player ignite TNT?
        if (event.hasBlock() && event.clickedBlock.type == TNT) {
          chain.add(event.clickedBlock) // Queue it up.

          // Cancel the event so another one isn't spawned.
          event.isCancelled = true
        }
      }

      else -> {
        if (event.hasBlock() && event.clickedBlock.type == TNT) {
          // We were just pretending.
          event.isCancelled = false
        }
      }
    }
  }

  @EventHandler(priority = LOWEST)
  fun BlockPlaceEventLowestPriorityHandler(event: BlockPlaceEvent) {
    // Is it TNT?
    if (event.blockPlaced.type == TNT) {
      // Pretend to cancel the event so that it isn't blocked by a protection plugin.
      event.isCancelled = true
    }
  }

  @EventHandler(priority = HIGHEST)
  fun BlockPlaceEventHighestPriorityHandler(event: BlockPlaceEvent) {
    // Is it TNT?
    if (event.blockPlaced.type == TNT) {
      // We were just pretending.
      event.isCancelled = false
    }
  }

  @EventHandler(priority = LOWEST)
  fun BlockBreakEventLowestPriorityHandler(event: BlockBreakEvent) {
    // Is it TNT?
    if (event.block.type == TNT) {
      // Pretend to cancel the event so that it isn't blocked by a protection plugin.
      event.isCancelled = true
    }
  }

  @EventHandler(priority = HIGHEST)
  fun BlockBreakEventHighestPriorityHandler(event: BlockBreakEvent) {
    // Is it TNT?
    if (event.block.type == TNT) {
      // We were just pretending.
      event.isCancelled = false
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
