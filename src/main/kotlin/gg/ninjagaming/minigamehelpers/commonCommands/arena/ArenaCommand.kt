package gg.ninjagaming.minigamehelpers.commonCommands.arena

import gg.ninjagaming.minigamehelpers.commonCommands.arena.ArenaCreateSubcommand.createArena
import gg.ninjagaming.minigamehelpers.commonCommands.arena.link.ArenaLinkSubCommand
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

/**
 * Handles the execution of the `/arena` command and its subcommands for managing arenas.
 *
 * This object serves as the command executor for the `/arena` command, providing functionality
 * for creating, viewing, deleting, toggling, setting world and spawn points, and managing the
 * configuration of arenas in the minigame environment. The command and its associated subcommands
 * are used to interact with arena data stored in the database.
 *
 * Key Subcommands:
 * - `create`: Creates a new arena with a given name and default configuration.
 * - `view`: Displays the details of a specific arena.
 * - `delete`: Deletes an existing arena from the system.
 * - `toggle`: Toggles the enabled state of an arena.
 * - `world`: Manages world-related settings for an arena.
 * - `spawnpoint`: Manages spawn points for an arena.
 *
 * Invalid commands or missing arguments will result in an appropriate usage message being
 * sent to the command sender.
 */
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
            "world" ->{
                if (args.size < 2)
                {
                    sender.sendMessage("Usage: /arena world <enter, center, set>")
                    return true
                }

                ArenaWorldSubCommand.worldSubCommand(sender, args)
            }

            "set-icon" -> {
                if (args.size < 3)
                {
                    sender.sendMessage("Usage: /arena set-icon <arenaName>")
                    return true
                }

                ArenaSetIconSubCommand.setIcon(args[1], sender)
            }


            "spawnpoint" -> {
                if (args.size < 2)
                {
                    sender.sendMessage("Usage: /arena spawnpoint <add, remove, list>")
                    return true
                }

                ArenaSpawnPointSubcommand.spawnPointSubCommand(sender, args)
            }

            "link" -> {
                if (args.size < 2)
                {
                    sender.sendMessage("Usage: /arena link <create, delete, list>")
                    return true
                }

                ArenaLinkSubCommand.linkSubCommand(sender, args)
            }

            "list" -> {
                ArenaListSubcommand.listArenas(sender)
            }

            else -> sender.sendMessage("Invalid usage. /arena <create, view, delete, list, link, toggle, world, spawnpoint>")
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