package gg.ninjagaming.minigamehelpers.commonCommands.arena

import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationTable
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.ktorm.dsl.eq
import org.ktorm.dsl.update

object ArenaSetIconSubCommand {
    fun setIcon(arenaName: String, sender: CommandSender) {
        if (sender !is Player){
            sender.sendMessage("You must be a player to run this command.")
            return
        }

        val iconMaterial = sender.inventory.itemInMainHand.type.name

        if (iconMaterial == "AIR"){
            sender.sendMessage("You must be holding an item to run this command.")
            return
        }

        val database = DatabaseHelper.getDatabase()

        val updated = database.update(ArenaConfigurationTable) {
            set(it.arenaIcon, iconMaterial)
            where {
                it.arenaName eq arenaName
            }
        }

        if (updated == 0) {
            sender.sendMessage("[OITC] Failed to set icon for arena $arenaName")
            return
        }

        sender.sendMessage("[OITC] Successfully set icon for arena $arenaName")
    }
}