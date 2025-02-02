package gg.ninjagaming.minigamehelpers.commonCommands.config

import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.GameModeConfigurationTable
import org.bukkit.command.CommandSender

import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList

object ConfigListSubCommand {
    fun listConfigs(player: CommandSender) :Boolean {
        val database = DatabaseHelper.getDatabase()

        var resultList = database.sequenceOf(GameModeConfigurationTable).toList()

        if(resultList.firstOrNull() == null){
            player.sendMessage("[OITC] No configs found.")
            return false
        }

        var foundConfigsString = "\n"

        for (result in resultList){
            foundConfigsString += ("    ------------------------------------------------\n")
            foundConfigsString += ("   Name: ${result.entryName}\n")
            foundConfigsString += ("    Enabled: ${result.isEnabled}\n")
            foundConfigsString += ("    Description: ${result.entryDescription}\n")
            foundConfigsString += ("    Default Lobby: ${result.defaultLobby}\n")
        }

        player.sendMessage("[OITC] Found the following configs:$foundConfigsString")

        return true
    }
}