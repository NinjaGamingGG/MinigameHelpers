package gg.ninjagaming.minigamehelpers.commonHelpers.itemBuilders

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object LobbyItemsBuilder {

    fun createLeaveLobbyItem(): ItemStack {
        val itemStack = ItemStack(Material.OAK_DOOR)
        val meta = itemStack.itemMeta
        meta.itemName(Component.text("§cLeave Lobby §7» Rightclick"))
        meta.lore(listOf(Component.text("§7Rightclick to go back to the hub")))
        itemStack.itemMeta = meta

        return itemStack
    }

    fun createArenaVoteItem(): ItemStack{
        val itemStack = ItemStack(Material.NAME_TAG)
        val meta = itemStack.itemMeta
        meta.itemName(Component.text("§6Vote Arena §7» Rightclick"))
        meta.lore(listOf(Component.text("§7Rightclick to vote for an arena")))
        itemStack.itemMeta = meta

        return itemStack
    }

}