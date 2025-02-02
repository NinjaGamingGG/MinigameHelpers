package gg.ninjagaming.minigamehelpers.commonCommands.arena

import org.bukkit.command.CommandSender

object ArenaViewSubcommand {
    fun viewArena(arenaName: String, sender: CommandSender) :Boolean {

        val entry = ArenaCommand.getArenaConfig(arenaName)

        if(entry == null){
            sender.sendMessage("[OITC] No config found with the name $arenaName")
            return false
        }

        sender.sendMessage("[OITC] Found the config $arenaName\n" +
                "   Description: ${entry.arenaDescription}\n" +
                "   Arena World: ${entry.arenaWorld}\n" +
                "   Arena Center: ${entry.arenaCenter}\n" +
                "   Arena Enabled: ${entry.arenaEnabled}\n" +
                "   Arena Icon: ${entry.arenaIcon}\n")

        return true
    }
}