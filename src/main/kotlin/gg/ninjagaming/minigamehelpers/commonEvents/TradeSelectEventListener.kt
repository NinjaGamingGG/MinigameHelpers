package gg.ninjagaming.minigamehelpers.commonEvents

import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.TradeSelectEvent

object TradeSelectEventListener: Listener {
    @EventHandler
    fun onTradeSelectEvent(event: TradeSelectEvent){

        val merchant = event.inventory.merchant
        val selectedTrade = merchant.recipes[event.index]

        val selectedArenaName = selectedTrade.ingredients[0].itemMeta.itemName()
        val plainArenaName = PlainTextComponentSerializer.plainText().serialize(selectedArenaName)

        val player = event.whoClicked as Player
        ArenaHelper.VoteManager.addVote(player, plainArenaName)

        player.sendMessage("You voted for $plainArenaName")
        player.closeInventory()
        event.isCancelled = true
    }
}