package gg.ninjagaming.minigamehelpers.commonCommands.config

import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.GameModeConfigurationTable
import org.bukkit.command.CommandSender
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq

object ConfigDeleteSubCommand {
    fun deleteConfig(configName: String, player: CommandSender) :Boolean {
        val database = DatabaseHelper.getDatabase()
        val rowsDeleted = database.delete(GameModeConfigurationTable) { it.entryName eq configName }

        if (rowsDeleted == 0) {
            player.sendMessage("[OITC] No config found with the name $configName")
            return false
        }

        player.sendMessage("[OITC] Successfully deleted the config $configName")
        return true
    }
}