package gg.ninjagaming.minigamehelpers.commonCommands.config

import gg.ninjaGaming.minigameOITC.commands.config.ConfigCreateSubcommand
import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.GameModeConfigurationEntry
import gg.ninjagaming.minigamehelpers.commonTables.GameModeConfigurationTable
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf

/**
 * Handles the configuration commands for a minigame. This object implements the `CommandExecutor` interface
 * to manage various operations on game mode configurations including creation, viewing, editing, loading,
 * deleting, listing, toggling, and setting player limits.
 *
 * The available commands are:
 * - `create`: Creates a new configuration with the provided name.
 * - `view`: Displays the details of a specific configuration.
 * - `edit`: Placeholder for editing functionality to be implemented later.
 * - `load`: Loads a configuration for use.
 * - `delete`: Removes a specified configuration.
 * - `list`: Lists all existing configurations.
 * - `toggle`: Toggles the enabled/disabled state of a configuration.
 * - `set-min-players`: Sets the minimum number of players for a specific configuration.
 * - `set-max-players`: Sets the maximum number of players for a specific configuration.
 * - `help`: Placeholder for providing detailed help to users.
 *
 * Each subcommand is delegated to a corresponding function or subcommand class for execution.
 * Additional helper function:
 * - `getGamemodeConfig`: Retrieves a game mode configuration by its name from the database.
 */
object ConfigCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("Usage: /config [create|view|edit|load|delete|list|help]")
            return true
        }

        when(args[0]){
            "create"-> {
                if(args.size < 2){
                    sender.sendMessage("Usage: /config create <configName>")
                    return true
                }
                return ConfigCreateSubcommand.createConfig(args[1], sender)
            }

            "view"-> {
                if(args.size < 2){
                    sender.sendMessage("Usage: /config view <configName>")
                    return true
                }
                return ConfigViewSubcommand.viewConfig(args[1], sender)
            }

            "edit"->{TODO("Implement later")
                println("Edit command not yet implemented")
            }

            "load"->{
                if(args.size < 2){
                    sender.sendMessage("Usage: /config load <configName>")
                    return true
                }

                return ConfigLoadSubcommand.loadConfig(args[1], sender)
            }

            "delete"->{
                if(args.size < 2){
                    sender.sendMessage("Usage: /config delete <configName>")
                    return true
                }
                return ConfigDeleteSubCommand.deleteConfig(args[1], sender)
            }

            "list"->{return ConfigListSubCommand.listConfigs(sender) }
            "toggle" -> {
                if(args.size < 2){
                    sender.sendMessage("Usage: /config toggle <configName>")
                    return true
                }
                return ConfigToggleSubcommand.toggleConfig(args[1], sender)
            }

            "description" ->{TODO("Implement later")
                println("Description command not yet implemented")
            }
            "set-min-players" ->{
                if (args.size < 3)
                {
                    sender.sendMessage("Usage: /config setMinPlayers <configName> <minPlayers>")
                    return true
                }

                ConfigSetMinPlayers.setMinPlayers(args[1], args[2].toInt(), sender)

            }
            "set-max-players" ->{
                if (args.size < 3)
                {
                    sender.sendMessage("Usage: /config setMaxPlayers <configName> <maxPlayers>")
                    return true
                }

                ConfigSetMaxPlayers.setMaxPlayers(args[1], args[2].toInt(), sender)
            }

            "help"->{TODO("Implement later")
            println("Help command not yet implemented")
            }

            else -> sender.sendMessage("Usage: /config [create|view|edit|load|delete|list|toggle|help]")
        }
        return true
    }

    fun getGamemodeConfig(configName: String) : GameModeConfigurationEntry? {
        val database = DatabaseHelper.getDatabase()

        return database.sequenceOf(GameModeConfigurationTable)
            .filter {GameModeConfigurationTable.entryName eq configName }
            .firstOrNull()
    }
}