package gg.ninjagaming.minigamehelpers.commonEvents

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

object PlayerDropItemEventListener: Listener {
    @EventHandler
    fun onPlayerDropItemEvent(event: PlayerDropItemEvent){
        event.isCancelled = true
    }
}