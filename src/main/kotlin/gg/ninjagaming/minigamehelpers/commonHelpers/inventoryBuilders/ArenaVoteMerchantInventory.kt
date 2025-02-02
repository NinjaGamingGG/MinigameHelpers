package gg.ninjagaming.minigamehelpers.commonHelpers.inventoryBuilders

import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe
import kotlin.collections.forEach

object ArenaVoteMerchantInventory {
    fun openInventory(player: Player) {
        val merchant = Bukkit.createMerchant(Component.text("Arena Vote"))
        val trades = mutableListOf<MerchantRecipe>()

        val arenaList = ArenaHelper.ArenaConfigManager.getArenaList()

        arenaList.forEach {

            val recipeItemStack = ItemStack(Material.GREEN_DYE)
            val recipeMeta = recipeItemStack.itemMeta
            recipeMeta.itemName(Component.text("Vote for ${it.arenaName}"))
            recipeMeta.lore(listOf(Component.text("Click to vote for ${it.arenaName}")))
            recipeItemStack.itemMeta = recipeMeta
            val arenaTrade = MerchantRecipe(ItemStack(recipeItemStack), 1)

            var iconMaterial = Material.getMaterial(it.arenaIcon)
            if (iconMaterial == null)
                iconMaterial = Material.DIRT

            val itemStack = ItemStack(iconMaterial)
            val meta = itemStack.itemMeta
            meta.itemName(Component.text(it.arenaName))
            meta.lore(listOf(Component.text(it.arenaDescription)))
            itemStack.itemMeta = meta
            arenaTrade.addIngredient(itemStack)
            trades.add(arenaTrade)
        }

        merchant.recipes = trades
        player.openMerchant(merchant, true)
    }

}
