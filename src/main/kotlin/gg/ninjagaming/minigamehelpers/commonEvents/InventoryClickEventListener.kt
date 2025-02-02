package gg.ninjagaming.minigamehelpers.commonEvents

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

object InventoryClickEventListener: Listener {
    @EventHandler
    fun onInventoryClick(eventHandler: InventoryClickEvent){
        eventHandler.isCancelled = true
    }
}