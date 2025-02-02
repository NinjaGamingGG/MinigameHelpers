package gg.ninjagaming.minigamehelpers.commonHelpers.inventoryBuilders

import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe
import kotlin.collections.forEach

/**
 * Provides functionality to create and open a custom merchant inventory for arena voting.
 *
 * This object generates an interactive merchant interface where players can vote
 * for different arenas through trade-like interactions. Each arena is represented
 * as a trade, where the player can select the arena they want to vote for.
 *
 * The merchant's inventory is dynamically populated based on the currently configured
 * arenas retrieved from `ArenaHelper.ArenaConfigManager.getArenaList`. Each arena is
 * visualized by its corresponding metadata, such as name, description, and icon.
 *
 * Usage of this object integrates with Bukkit's API to handle the display of merchant
 * inventories to players, allowing a seamless voting experience. The visual and functional
 * design of the inventory ensures that players easily understand and interact with the voting process.
 *
 * Functionality in this object is designed to work as part of a larger minigame system,
 * enabling efficient extensions for voting mechanisms during the game's lobby phase.
 */
object ArenaVoteMerchantInventory {
    /**
     * Opens a custom merchant inventory for the player, allowing them to vote for arenas.
     * The inventory displays available arenas as trade options, where players can interact
     * and select their preferred arena for voting.
     *
     * @param player The player for whom the inventory will be opened.
     */
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
