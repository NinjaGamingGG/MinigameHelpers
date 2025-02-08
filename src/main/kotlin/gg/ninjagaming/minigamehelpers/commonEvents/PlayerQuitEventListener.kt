package gg.ninjagaming.minigamehelpers.commonEvents

import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper
import gg.ninjagaming.minigamehelpers.commonHelpers.GameState
import gg.ninjagaming.minigamehelpers.commonHelpers.GameStateHelper
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent



/**
 * Event listener for handling the `PlayerQuitEvent`.
 *
 * This listener is triggered whenever a player quits the server. It performs cleanup
 * operations and game state management based on the current game state and remaining players.
 *
 * Key functionalities include:
 * - Removing the player from the list of active players and spectators using
 *   `ArenaHelper.PlayerManagement.removePlayer` and `ArenaHelper.PlayerManagement.removeSpectator`.
 * - Removing the player's vote from the arena voting system using `ArenaHelper.VoteManager.removeVote`.
 * - Setting a custom quit message using the player's name.
 * - Handling the server shutdown if no players are online during the `PLAYING` game state.
 * - Advancing the game state to `ENDING` if only one player remains during the `PLAYING` state.
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

        if (GameStateHelper.getGameState() != GameState.PLAYING)
            return

        if (Bukkit.getServer().onlinePlayers.isEmpty()){
            Bukkit.shutdown()
            return
        }

        if (Bukkit.getServer().onlinePlayers.size == 1){
            GameStateHelper.advanceState()
        }
    }
}