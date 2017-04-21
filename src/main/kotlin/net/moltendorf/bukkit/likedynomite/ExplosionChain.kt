package net.moltendorf.bukkit.likedynomite

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.EntityType
import org.bukkit.scheduler.BukkitTask

/**
 * Created by moltendorf on 2017-04-18.
 */

class ExplosionChain {
  val size: Int
    get() = primed.size

  private val primed = LinkedHashSet<Block>()
  private var primingTask: BukkitTask? = null
  private var statusTask: BukkitTask? = null

  fun add(block: Block) {
    primed.add(block)

    start()
  }

  fun addAll(blocks: Collection<Block>) {
    primed.addAll(blocks)

    start()
  }

  fun primeTNT() {
    for (i in 1..settings.rate) {
      val block = primed.first()

      if (block.type === Material.TNT) {
        val location = block.location.add(0.5, 0.0, 0.5)
        val world = location.world

        block.type = Material.AIR // Remove the TNT from the world.

        world.playSound(location, Sound.ENTITY_TNT_PRIMED, 1f, 1f)
        world.spawnEntity(location, EntityType.PRIMED_TNT) // Bypass protections by manually spawning a primed TNT.
      } else {
        i.dec()
      }

      primed.remove(block)

      if (primed.isEmpty()) {
        primingTask?.cancel()
        primingTask = null
        statusTask?.cancel()
        statusTask = null

        return
      }
    }
  }

  private fun start() {
    if (primingTask === null && primed.isNotEmpty()) {
      primingTask = scheduler.runTaskTimer(instance, { primeTNT() }, 1, 1)
    }

    if (statusTask === null && primed.size > 1_000) {
      statusTask = scheduler.runTaskTimer(instance, { i { "Queued TNT: ${primed.size}." } }, 20 * 10, 20 * 10)
    }
  }
}
