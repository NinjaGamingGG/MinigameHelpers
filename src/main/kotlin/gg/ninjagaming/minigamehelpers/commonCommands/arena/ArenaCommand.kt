package gg.ninjagaming.minigamehelpers.commonCommands.arena

import gg.ninjagaming.minigamehelpers.commonCommands.arena.ArenaCreateSubcommand.createArena
import gg.ninjagaming.minigamehelpers.commonCommands.arena.spawnpoint.ArenaSpawnPointSubcommand
import gg.ninjagaming.minigamehelpers.commonCommands.arena.world.ArenaWorldSubCommand
import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationEntry
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationTable
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import kotlin.collections.isEmpty
import kotlin.text.lowercase

object ArenaCommand: CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: (Array<out String>)): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("Invalid usage. /arena <create, view, delete, toggle, world, spawnpoint>")
            return true
        }

        when(args[0].lowercase()){
            "create" -> {
                if(args.size < 2){
                    sender.sendMessage("Usage: /arena create <arenaName>")
                    return true
                }

                createArena(args[1], sender)
            }
            "view" -> {
                if (args.size < 2) {
                    sender.sendMessage("Usage: /arena view <arenaName>")
                    return true
                }

                ArenaViewSubcommand.viewArena(args[1], sender)
            }
            "delete" -> {
                if (args.size < 2) {
                    sender.sendMessage("Usage: /arena delete <arenaName>")
                    return true
                }

                ArenaDeleteSubcommand.deleteArena(args[1], sender)
            }
            "toggle" ->{
                if (args.size < 2)
                {
                    sender.sendMessage("Usage: /arena toggle <arenaName>")
                    return true
                }

                ArenaToggleEnabledSubcommand.toggleEnabled(args[1], sender)
            }
            "world" -> ArenaWorldSubCommand.worldSubCommand(sender, args)

            "set-icon" -> {
                if (args.size < 3)
                {
                    sender.sendMessage("Usage: /arena set-icon <arenaName>")
                    return true
                }

                ArenaSetIconSubCommand.setIcon(args[1], sender)
            }


            "spawnpoint" -> ArenaSpawnPointSubcommand.spawnPointSubCommand(sender, args)
            else -> sender.sendMessage("Invalid usage. /arena <create, view, delete, toggle, world, spawnpoint>")
        }
        return true
    }

    fun getArenaConfig(configName: String) : ArenaConfigurationEntry? {
        val database = DatabaseHelper.getDatabase()

        return database.sequenceOf(ArenaConfigurationTable)
            .filter { ArenaConfigurationTable.arenaName eq configName }
            .firstOrNull()
    }
}