package gg.ninjagaming.minigamehelpers.commonEvents

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Event listener for handling the `InventoryClickEvent`.
 *
 * This listener is triggered whenever a player interacts with an inventory by clicking
 * on an item. Its primary purpose is to restrict any interaction within certain inventories
 * by canceling the event. This ensures that players cannot modify or interact with items
 * in inventories meant to be immutable during gameplay.
 *
 * Key functionality includes:
 * - Automatically canceling the inventory click event to prevent unauthorized interactions.
 */
object InventoryClickEventListener: Listener {
    @EventHandler
    fun onInventoryClick(eventHandler: InventoryClickEvent){
        eventHandler.isCancelled = true
    }
}