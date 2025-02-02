package gg.ninjagaming.minigamehelpers.commonCommands.arena.world

import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationTable
import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import org.bukkit.command.CommandSender
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import java.io.File

object ArenaWorldSetSubcommand {
    fun setWorld(arenaName: String, worldName: String, sender: CommandSender) :Boolean {

        if (!File("./$worldName").exists())
        {
            sender.sendMessage("[OITC] The world $worldName does not exist")
            return false
        }

        val newWorld = Bukkit.createWorld(WorldCreator(worldName))

        Bukkit.getWorlds().add(newWorld)

        val database = DatabaseHelper.getDatabase()

        val updated = database.update(ArenaConfigurationTable) {
            set(it.arenaWorld, worldName)
            where {
                it.arenaName eq arenaName
            }
        }

        if (updated == 0) {
            sender.sendMessage("[OITC] Failed to set world for arena $arenaName")
            return false
        }

        sender.sendMessage("[OITC] Successfully set world for arena $arenaName")
        return true
    }
}