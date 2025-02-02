package gg.ninjagaming.minigamehelpers.commonEvents

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

object EntityPickupItemEventListener: Listener {
    @EventHandler
    fun onPlayerPickupItemEvent(event: EntityPickupItemEvent){
        event.isCancelled = true
    }
}