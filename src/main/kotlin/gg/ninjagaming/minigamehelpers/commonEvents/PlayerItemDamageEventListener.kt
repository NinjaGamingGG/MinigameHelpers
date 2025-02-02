package gg.ninjagaming.minigamehelpers.commonEvents

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemDamageEvent

object PlayerItemDamageEventListener: Listener {
    @EventHandler
    fun onPlayerItemDamageEvent(event: PlayerItemDamageEvent){
        event.isCancelled = true
    }
}