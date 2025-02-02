package gg.ninjagaming.minigamehelpers.commonCommands.arena

import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationTable
import org.bukkit.command.CommandSender
import org.ktorm.dsl.eq
import org.ktorm.dsl.update

object ArenaToggleEnabledSubcommand {
    fun toggleEnabled(arenaName: String, sender: CommandSender) : Boolean {
        val entry = ArenaCommand.getArenaConfig(arenaName)

        if (entry == null)
        {
            sender.sendMessage("[OITC] No config found with the name $arenaName")
            return false
        }


        val isCurrentlyDisabled = entry.arenaEnabled == 0
        val newEnabledState = if (isCurrentlyDisabled) 1 else 0

        val database = DatabaseHelper.getDatabase()

        val updatedRows = database.update(ArenaConfigurationTable){
            set(it.arenaEnabled, newEnabledState)
            where { it.arenaName eq arenaName }
        }

        if(updatedRows == 0)
        {
            sender.sendMessage("[OITC] Failed to toggle enabled state for arena $arenaName")
            return false
        }

        sender.sendMessage("[OITC] Successfully toggled enabled state for arena $arenaName")
        return true
    }
}