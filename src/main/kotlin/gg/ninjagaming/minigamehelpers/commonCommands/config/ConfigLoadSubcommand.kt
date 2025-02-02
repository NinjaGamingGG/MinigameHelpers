package gg.ninjagaming.minigamehelpers.commonCommands.config

import gg.ninjagaming.minigamehelpers.MinigameHelpers
import org.bukkit.command.CommandSender


object ConfigLoadSubcommand {
    fun loadConfig(configName: String, player: CommandSender) :Boolean {
        var result = ConfigCommand.getGamemodeConfig(configName)

        if (result == null){
            player.sendMessage("[OITC] No config found with the name $configName")
            return false
        }

        MinigameHelpers.setMinigameConfig(result)

        player.sendMessage("[OITC] Successfully loaded the config $configName")

        return true
    }
}