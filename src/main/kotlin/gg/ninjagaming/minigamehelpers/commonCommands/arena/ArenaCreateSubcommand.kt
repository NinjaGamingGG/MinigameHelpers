package gg.ninjagaming.minigamehelpers.commonCommands.arena


import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationTable
import org.bukkit.command.CommandSender
import org.ktorm.dsl.insert

object ArenaCreateSubcommand {
    fun createArena(arenaName: String, sender: CommandSender) : Boolean{
        val database = DatabaseHelper.getDatabase()

        val inserted = database.insert(ArenaConfigurationTable){
            set(it.arenaName, arenaName)
            set(it.arenaDescription, "New Arena")
            set(it.arenaWorld, "")
            set(it.arenaCenter, "0,0")
            set(it.arenaEnabled,0)
            set(it.arenaIcon, "GRASS_BLOCK")
        }

        if(inserted == 0){
            sender.sendMessage("[OITC] Failed to create arena $arenaName")
            return false
        }

        sender.sendMessage("[OITC] Successfully created the arena $arenaName")
        return true
    }
}