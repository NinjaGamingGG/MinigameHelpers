package gg.ninjaGaming.minigameOITC.commands.config

import gg.ninjagaming.minigamehelpers.MinigameHelpers
import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.GameModeConfigurationTable
import org.bukkit.command.CommandSender
import org.ktorm.dsl.insert

object ConfigCreateSubcommand {
    fun createConfig(configName: String, player: CommandSender) :Boolean {

        val database = DatabaseHelper.getDatabase()
        val inserted = database.insert(GameModeConfigurationTable){
            set(it.entryName, configName)
            set(it.entryDescription, "New Entry")
            set(it.defaultLobby, "")
            set(it.isEnabled, 0)
            set(it.minimumPlayers, 2)
            set(it.maximumPlayers, 8)
        }

        if(inserted == 0){
            MinigameHelpers.getPluginInstance().logger.warning ("Failed to create config $configName")
            player.sendMessage("[OITC] Failed to create config $configName")
            return false
        }

        player.sendMessage("[OITC] Successfully created the config $configName")

        return true
    }

}