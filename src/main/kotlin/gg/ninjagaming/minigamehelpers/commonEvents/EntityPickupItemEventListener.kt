package gg.ninjagaming.minigamehelpers.commonEvents

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

/**
 * Event listener for handling the `EntityPickupItemEvent`.
 *
 * This listener is triggered whenever an entity attempts to pick up an item.
 * The primary purpose is to prevent entities, including players, from collecting items,
 * ensuring that items remain in the game world and are not added to inventories.
 *
 * Key functionality includes:
 * - Automatically canceling the item pickup event to restrict entities from picking up items.
 */
object EntityPickupItemEventListener: Listener {
    @EventHandler
    fun onPlayerPickupItemEvent(event: EntityPickupItemEvent){
        event.isCancelled = true
    }
}