package gg.ninjagaming.minigamehelpers.commonEvents

import gg.ninjagaming.minigamehelpers.MinigameHelpers
import gg.ninjagaming.minigamehelpers.commonHelpers.inventoryBuilders.ArenaVoteMerchantInventory
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

/**
 * Listener for handling player interaction events (`PlayerInteractEvent`).
 *
 * This listener is triggered whenever a player performs an interaction, such as right-clicking
 * with an item or on a block. The listener responds to certain interactions based on the item the
 * player is holding and executes specific actions accordingly.
 *
 * Key functionalities include:
 * - Identifying right-click actions (`RIGHT_CLICK_AIR` or `RIGHT_CLICK_BLOCK`).
 * - Handling specific items such as:
 *   - `Material.OAK_DOOR`: Executes the leave lobby action and kicks the player from the lobby.
 *   - `Material.NAME_TAG`: Opens a custom inventory for arena voting, enabling players to vote
 *     for their preferred arena.
 */
object PlayerInteractEventListener: Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {

        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK){
            rightClickAction(event)
        }
    }

    private fun rightClickAction(event: PlayerInteractEvent) {

        val clickedItemStack = event.player.inventory.itemInMainHand

        when(clickedItemStack.type){
            Material.OAK_DOOR -> leaveLobbyAction(event)
            Material.NAME_TAG -> mapVoteAction(event)
            else -> return
        }
    }

    private fun mapVoteAction(event: PlayerInteractEvent) {
        if (MinigameHelpers.getPluginConfig().getBoolean("lobby.Allow_Arena_Vote"))
            ArenaVoteMerchantInventory.openInventory(event.player)
    }

    private fun leaveLobbyAction(event: PlayerInteractEvent) {
        event.player.kick(Component.text("You have left the lobby."))
    }
}