package gg.ninjagaming.minigamehelpers.commonCommands.config

import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.GameModeConfigurationTable
import org.bukkit.command.CommandSender
import org.ktorm.dsl.eq
import org.ktorm.dsl.update


object ConfigToggleSubcommand {
    fun toggleConfig(configName: String, player: CommandSender) :Boolean {

        val entry = ConfigCommand.getGamemodeConfig(configName)

        if (entry == null){
            player.sendMessage("[OITC] No config found with the name $configName")
            return false
        }

        var newEnabledState = 0
        if (entry.isEnabled == 0){
            newEnabledState = 1
        }

        val database = DatabaseHelper.getDatabase()

        val updatedRows = database.update(GameModeConfigurationTable){
            set(it.isEnabled, newEnabledState)
            where { it.entryName eq configName }
        }

        if (updatedRows == 0){
            player.sendMessage("[OITC] No config found with the name $configName")
            return false
        }

        player.sendMessage("[OITC] Successfully toggled the config $configName")
        return true
    }
}