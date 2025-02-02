package gg.ninjagaming.minigamehelpers.commonEvents

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

/**
 * Event listener for handling the `PlayerDropItemEvent`.
 *
 * This listener is triggered whenever a player attempts to drop an item. Its primary purpose
 * is to prevent players from dropping items within the game, ensuring items remain within
 * the player's inventory at all times.
 *
 * Key functionality includes:
 * - Automatically canceling the item drop event to restrict players from dropping items.
 */
object PlayerDropItemEventListener: Listener {
    @EventHandler
    fun onPlayerDropItemEvent(event: PlayerDropItemEvent){
        event.isCancelled = true
    }
}