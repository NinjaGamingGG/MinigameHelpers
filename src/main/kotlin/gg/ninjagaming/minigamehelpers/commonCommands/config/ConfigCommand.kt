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