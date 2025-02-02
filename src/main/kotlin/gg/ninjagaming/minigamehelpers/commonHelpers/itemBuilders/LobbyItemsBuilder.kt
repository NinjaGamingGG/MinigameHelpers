package gg.ninjagaming.minigamehelpers.commonHelpers.itemBuilders

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Provides functionality to create and manage items used in the lobby for players.
 * This object focuses on creating specific items for player interactions in the lobby,
 * such as leaving the lobby or voting for an arena.
 */
object LobbyItemsBuilder {

    /**
     * Creates an ItemStack representing the lobby leave item.
     * This item is an oak door with a custom name and lore,
     * intended to be used for players to leave the lobby.
     *
     * @return An ItemStack configured as the leave lobby item.
     */
    fun createLeaveLobbyItem(): ItemStack {
        val itemStack = ItemStack(Material.OAK_DOOR)
        val meta = itemStack.itemMeta
        meta.itemName(Component.text("§cLeave Lobby §7» Rightclick"))
        meta.lore(listOf(Component.text("§7Rightclick to go back to the hub")))
        itemStack.itemMeta = meta

        return itemStack
    }

    /**
     * Creates an ItemStack representing the arena vote item.
     * This item is a name tag with a custom name and lore, intended for players to use
     * to vote for an arena during the lobby phase of the game.
     *
     * @return An ItemStack configured as the arena vote item.
     */
    fun createArenaVoteItem(): ItemStack{
        val itemStack = ItemStack(Material.NAME_TAG)
        val meta = itemStack.itemMeta
        meta.itemName(Component.text("§6Vote Arena §7» Rightclick"))
        meta.lore(listOf(Component.text("§7Rightclick to vote for an arena")))
        itemStack.itemMeta = meta

        return itemStack
    }

}