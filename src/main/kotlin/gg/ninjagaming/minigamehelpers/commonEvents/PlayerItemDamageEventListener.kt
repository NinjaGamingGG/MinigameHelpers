package gg.ninjagaming.minigamehelpers.commonEvents

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemDamageEvent

/**
 * An event listener for handling `PlayerItemDamageEvent`.
 *
 * This listener is intended to prevent the damage of items held by players. When triggered,
 * it cancels the event to ensure no item damage occurs.
 *
 * Key functionalities include:
 * - Listening for `PlayerItemDamageEvent`.
 * - Canceling the event to preserve the durability of the player's item.
 */
object PlayerItemDamageEventListener: Listener {
    @EventHandler
    fun onPlayerItemDamageEvent(event: PlayerItemDamageEvent){
        event.isCancelled = true
    }
}