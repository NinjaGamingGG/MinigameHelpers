package gg.ninjagaming.minigamehelpers.commonEvents

import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object PlayerQuitEventListener: Listener {
    @EventHandler
    fun onPlayerQuitEvent(event: PlayerQuitEvent){
        if (ArenaHelper.PlayerManagement.getPlayerList().contains(event.player))
            ArenaHelper.PlayerManagement.removePlayer(event.player)
        if (ArenaHelper.PlayerManagement.getSpectatorList().contains(event.player))
            ArenaHelper.PlayerManagement.removeSpectator(event.player)

        ArenaHelper.VoteManager.removeVote(event.player)

        event.quitMessage(Component.text("§c- §r${event.player.name}"))

    }
}