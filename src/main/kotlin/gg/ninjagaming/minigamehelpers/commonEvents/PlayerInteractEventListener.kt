package gg.ninjagaming.minigamehelpers.commonEvents

import gg.ninjagaming.minigamehelpers.MinigameHelpers
import gg.ninjagaming.minigamehelpers.commonHelpers.inventoryBuilders.ArenaVoteMerchantInventory
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

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