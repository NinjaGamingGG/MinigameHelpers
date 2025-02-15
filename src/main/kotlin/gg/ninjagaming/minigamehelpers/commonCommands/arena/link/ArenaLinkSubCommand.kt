package gg.ninjagaming.minigamehelpers.commonCommands.arena.link

import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.ConfigArenaLinkTable
import org.bukkit.command.CommandSender
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.entity.count
import org.ktorm.entity.forEach
import org.ktorm.entity.sequenceOf

object ArenaLinkSubCommand {
    fun linkSubCommand(sender: CommandSender, args: Array<out String>) {
        when (args[1]) {
            "create" -> {
                if (args.size < 4) {
                    sender.sendMessage("Invalid usage. /arena link create <arenaId> <configId>")
                    return
                }

                val database = DatabaseHelper.getDatabase()

                val inserted = database.insert(ConfigArenaLinkTable) {
                    set(it.arenaId, args[2].toInt())
                    set(it.configId, args[3].toInt())
                }

                if (inserted == 0) {
                    sender.sendMessage("[OITC] Failed to create link.")
                    return
                }

                sender.sendMessage("[OITC] Successfully created link.")
            }

            "list" -> {
                val database = DatabaseHelper.getDatabase()

                val arenaLinks = database.sequenceOf(ConfigArenaLinkTable)

                if (arenaLinks.count() == 0) {
                    sender.sendMessage("[OITC] No Config->Arena Links found.")
                    return
                }

                var foundConfigString = "\n"
                arenaLinks.forEach {
                    foundConfigString += ("    ------------------------------------------------\n")
                    foundConfigString += ("    EntryId: ${it.entryId}\n")
                    foundConfigString += ("    ConfigId: ${it.configId}\n")
                    foundConfigString += ("    ArenaId: ${it.arenaId}\n")
                }

                sender.sendMessage("[OITC] Found the following Config->Arena Links:\n $foundConfigString")
            }

            "remove" -> {
                if (args.size < 3) {
                    sender.sendMessage("Invalid usage. /arena link remove <id>")
                    return
                }

                val database = DatabaseHelper.getDatabase()

                val deleted = database.delete(ConfigArenaLinkTable) {
                    it.entryId eq args[2].toInt()
                }

                if (deleted == 0) {
                    sender.sendMessage("[OITC] No link found with id ${args[2]}")
                    return
                }

                sender.sendMessage("[OITC] Successfully removed the link with id ${args[2]}")
            }

            else -> sender.sendMessage("Invalid usage. /arena link <create, list, remove>")
        }
    }
}