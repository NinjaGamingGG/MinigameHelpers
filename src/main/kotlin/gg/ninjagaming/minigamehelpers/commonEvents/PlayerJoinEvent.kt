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