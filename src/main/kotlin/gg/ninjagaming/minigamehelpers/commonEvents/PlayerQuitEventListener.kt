package gg.ninjagaming.minigamehelpers.commonEvents

import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

/**
 * An event listener for handling the `PlayerQuitEvent`.
 *
 * This listener is triggered whenever a player leaves the server. It ensures that the player
 * is removed from all relevant data structures such as player lists, spectator lists, and vote
 * records. Additionally, it customizes the quit message to indicate the player has left.
 *
 * Key functionalities include:
 * - Removing the player from the active player list if they were participating in the game.
 * - Removing the player from the spectator list if they were spectating.
 * - Removing the player's vote from the VoteManager system.
 * - Sending a formatted quit message to notify other players about the disconnection.
 */
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