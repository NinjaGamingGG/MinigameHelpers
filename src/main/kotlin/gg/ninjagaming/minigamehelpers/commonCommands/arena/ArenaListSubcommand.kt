package gg.ninjagaming.minigamehelpers.commonCommands.arena

import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationTable
import org.bukkit.command.CommandSender
import org.ktorm.dsl.from
import org.ktorm.entity.find
import org.ktorm.entity.forEach
import org.ktorm.entity.sequenceOf

object ArenaListSubcommand {
    fun listArenas(sender: CommandSender){
        val database = DatabaseHelper.getDatabase()

        val arenas = database.sequenceOf(ArenaConfigurationTable)

        var foundArenasString = "\n"

        arenas.forEach {
            foundArenasString += "____________________________________________________________\n"
            foundArenasString += "Arena Id: ${it.entryId}"
            foundArenasString += "Arena: ${it.arenaName}\n"
            foundArenasString += "Description: ${it.arenaDescription}\n"
            foundArenasString += "World: ${it.arenaWorld}\n"
            foundArenasString += "Center: ${it.arenaCenter}\n"
            foundArenasString += "Enabled: ${it.arenaEnabled}\n"
            foundArenasString += "Icon: ${it.arenaIcon}\n"
        }

        sender.sendMessage("Found arenas: $foundArenasString")

    }
}