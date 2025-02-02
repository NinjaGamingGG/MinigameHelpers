package gg.ninjagaming.minigamehelpers.commonEvents

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerSwapHandItemsEvent

/**
 * An event listener for handling the `PlayerSwapHandItemsEvent`.
 *
 * This listener is triggered whenever the player attempts to swap items between their main hand
 * and offhand. When this event occurs, it is automatically cancelled to prevent the swapping action.
 *
 * This can be useful in scenarios such as game modes or plugins where swapping items between hands
 * is restricted or not required.
 */
object PlayerSwapHandItemsEventListener: Listener {
    @EventHandler
    fun onPlayerSwapHandItemsEvent(event: PlayerSwapHandItemsEvent){
        event.isCancelled = true
    }
}