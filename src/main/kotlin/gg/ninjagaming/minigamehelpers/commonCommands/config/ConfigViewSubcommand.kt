package gg.ninjagaming.minigamehelpers.commonCommands.config

import org.bukkit.command.CommandSender

object ConfigViewSubcommand {
    fun viewConfig(configName: String, player: CommandSender) :Boolean {

        var entry = ConfigCommand.getGamemodeConfig(configName)

        if (entry == null){
            player.sendMessage("[OITC] No config found with the name $configName")
            return false
        }

        player.sendMessage("[OITC] Found the following config: \n" +
                "   Name: $configName\n" +
                "   Enabled: ${entry.isEnabled}\n" +
                "   Description: ${entry.entryDescription}\n" +
                "   Default Lobby: ${entry.defaultLobby}" +
                "   Minimal Players: ${entry.minimumPlayers}\n" +
                "   Maximal Players: ${entry.maximumPlayers}")

        return true
    }

}