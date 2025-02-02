package gg.ninjagaming.minigamehelpers.commonEvents

import gg.ninjagaming.minigamehelpers.MinigameHelpers
import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper
import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper.playerLobbyTeleport
import gg.ninjagaming.minigamehelpers.commonHelpers.GameState
import gg.ninjagaming.minigamehelpers.commonHelpers.GameStateHelper
import gg.ninjagaming.minigamehelpers.commonHelpers.itemBuilders.LobbyItemsBuilder
import net.kyori.adventure.text.Component
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * Event listener for handling the `PlayerJoinEvent`.
 *
 * This listener is triggered whenever a player joins the server. It handles the player's actions
 * upon joining, such as determining the game state, adding the player to the appropriate list,
 * setting up their inventory, teleporting them to the lobby, or placing them in spectator mode.
 * The join message is also updated to reflect the addition of the player.
 *
 * Key functionalities include:
 * - Determining the game state using `GameStateHelper.getGameState`.
 * - Displaying a customized join message with the player's name.
 * - Handling players joining during the `LOBBY` state:
 *   - Adding the player to the active lobby player list.
 *   - Configuring the player's inventory with items such as arena voting and lobby leave items.
 *   - Teleporting the player to the lobby's configured spawn location.
 *   - Setting the player's game mode to adventure mode.
 * - Handling players joining during other game states:
 *   - Checking if spectator mode is enabled in the plugin's configuration.
 *   - Adding the player to the spectator list if spectator mode is enabled.
 */
object PlayerJoinEvent: Listener {
    @EventHandler
    fun onPlayerJoinEvent(event: PlayerJoinEvent){
        val currentGameState = GameStateHelper.getGameState()

        event.joinMessage(Component.text("§a+ §r${event.player.name}"))

        when(currentGameState){
            GameState.LOBBY -> {
                playerJoinLobby(event.player)
                playerAddLobbyItems(event.player)
                playerLobbyTeleport(event.player)
                setPlayerGamemode(event.player)
            }

            else -> {
                val spectatorEnabled = MinigameHelpers.getPluginConfig().getBoolean("gamemode.spectatorEnabled")
                if (!spectatorEnabled)
                    return

                playerJoinSpectator(event.player)
            }
        }


    }

    private fun playerJoinLobby(player :Player){
        ArenaHelper.PlayerManagement.addPlayer(player)
    }

    private fun playerAddLobbyItems(player: Player){
        player.inventory.clear()
        player.inventory.setItem(8,LobbyItemsBuilder.createLeaveLobbyItem())
        if (MinigameHelpers.getPluginConfig().getBoolean("lobby.Allow_Arena_Vote"))
            player.inventory.setItem(4, LobbyItemsBuilder.createArenaVoteItem())
    }

    private fun setPlayerGamemode(player: Player)
    {
        player.gameMode = GameMode.ADVENTURE
    }

    private fun playerJoinSpectator(player: Player){
        ArenaHelper.PlayerManagement.addSpectator(player)
    }
}