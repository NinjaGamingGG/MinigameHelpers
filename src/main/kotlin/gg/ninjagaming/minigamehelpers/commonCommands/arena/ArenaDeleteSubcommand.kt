package gg.ninjagaming.minigamehelpers.commonCommands.arena

import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationTable
import org.bukkit.command.CommandSender
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq

object ArenaDeleteSubcommand {
    fun deleteArena(arenaName: String, sender: CommandSender) : Boolean {
        val database = DatabaseHelper.getDatabase()

        val rowsDeleted = database.delete(ArenaConfigurationTable) { it.arenaName eq arenaName }

        if (rowsDeleted == 0) {
            sender.sendMessage("[OITC] Failed to delete arena $arenaName")
            return false
        }

        sender.sendMessage("[OITC] Successfully deleted the arena $arenaName")

        return true
    }
}