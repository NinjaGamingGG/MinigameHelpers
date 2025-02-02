package gg.ninjagaming.minigamehelpers.commonCommands.config

import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.GameModeConfigurationTable
import org.bukkit.command.CommandSender
import org.ktorm.dsl.eq
import org.ktorm.dsl.update

object ConfigSetMaxPlayers {
    fun setMaxPlayers(configName: String, minPlayers: Int, sender: CommandSender) :Boolean{
        val database = DatabaseHelper.getDatabase()

        val updatedRows = database.update(GameModeConfigurationTable){
            set(it.maximumPlayers, minPlayers)
            where { it.entryName eq configName }
        }

        if(updatedRows == 0)
        {
            sender.sendMessage("[OITC] Failed to set the maximum players for config $configName")
            return false
        }

        sender.sendMessage("[OITC] Successfully set the maximum players for config $configName")

        return true
    }
}