package net.moltendorf.bukkit.likedynomite

import org.bukkit.Server
import org.bukkit.configuration.Configuration
import org.bukkit.scheduler.BukkitScheduler

/**
 * Created by moltendorf on 2017-04-18.
 */

internal inline val enabled: Boolean
  get() = LikeDynomite.enabled

internal inline val instance: LikeDynomite
  get() = LikeDynomite.instance

internal inline val config: Configuration
  get() = instance.config

internal inline val server: Server
  get() = instance.server

internal inline val scheduler: BukkitScheduler
  get() = server.scheduler

internal inline val settings: Settings
  get() = instance.settings

internal fun broadcast(s: String) {
  server.broadcastMessage(s)
}

internal fun console(s: String) {
  server.consoleSender.sendMessage("[${instance.name}] $s")
}

internal inline fun i(s: () -> String) {
  instance.logger.info(s.invoke())
}

internal inline fun f(s: () -> String) {
  instance.logger.fine(s.invoke())
}
