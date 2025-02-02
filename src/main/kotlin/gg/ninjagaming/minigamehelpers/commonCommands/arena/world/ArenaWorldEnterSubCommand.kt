package gg.ninjagaming.minigamehelpers.commonCommands.arena.world


import gg.ninjagaming.minigamehelpers.commonCommands.arena.ArenaCommand
import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File

object ArenaWorldEnterSubCommand {
    fun enterWorld(arenaName: String, sender: CommandSender) :Boolean {
        val entry = ArenaCommand.getArenaConfig(arenaName)

        if (sender !is Player) {
            sender.sendMessage("[OITC] You must be a player to do this.")
            return false
        }


        if (entry == null) {
            sender.sendMessage("[OITC] That arena doesn't exist.")
            return false
        }

        if (!File("./${entry.arenaWorld}").exists())
        {
            sender.sendMessage("[OITC] The world ${entry.arenaWorld} does not exist")
            return false
        }

        sender.sendMessage("[OITC] Loading world ${entry.arenaWorld}...")

        val newWorld = ArenaHelper.prepareWorld(entry)

        if (newWorld == null) {
            sender.sendMessage("[OITC] Failed to load world ${entry.arenaWorld}")
            return false
        }

        sender.teleport(newWorld.spawnLocation)

        return true
    }
}