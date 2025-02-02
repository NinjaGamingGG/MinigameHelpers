package gg.ninjagaming.minigamehelpers.commonCommands.arena

import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import kotlin.text.startsWith

/**
 * Provides tab-completion functionality for the `/arena` command and its subcommands.
 *
 * This object is responsible for assisting users by suggesting relevant subcommands
 * and arguments during the typing of `/arena` commands in the chat. Tab completion
 * is determined based on the current input and the context of the arguments provided.
 *
 * Tab Completion Hierarchy:
 * - First-level subcommands include `create`, `view`, `delete`, `toggle`, `world`, `set-icon`, and `spawnpoint`.
 * - Second-level subcommands:
 *   - For `spawnpoint`: `add`, `remove`, and `list`.
 *   - For `world`: `set`, `center`, and `enter`.
 *   - Arena names are suggested for applicable subcommands at the second level.
 *
 * Behavior:
 * - When no arguments or an empty string is provided as input, all top-level subcommands are suggested.
 * - For incomplete first-level commands, suggestions are filtered and matched against user input.
 * - For second-level arguments, subcommands or arena names are filtered and suggested based on the context of the first-level subcommand.
 * - If the `create` subcommand is provided as the first argument, no further suggestions are offered.
 *
 * Returns `null` if the provided command is not `arena` or if no relevant suggestions are available
 * for the given input.
 */
object ArenaCommandTabCompleter: TabCompleter {
    override fun onTabComplete(
        sender: CommandSender, comand: Command, alias: String, args: (Array<out String>)): List<String?>? {

        if (!comand.name.equals("arena", ignoreCase = true))
            return null

        if (args.isEmpty() || args[0] == "")
            return FIRST_LEVEL_SUB_COMMANDS

        return when (args.size) {
            1 -> firstLevelArgs(args)
            2 -> secondLevelArgs(args)
            else -> null
        }


        return null
    }

    private val FIRST_LEVEL_SUB_COMMANDS = listOf(
        "create", "view", "delete", "toggle", "world", "set-icon" , "spawnpoint"
    )

    private val SECOND_LEVEL_SPAWNPOINT_SUB_COMMANDS = listOf("add", "remove", "list")
    private val SECOND_LEVEL_WORLD_SUB_COMMANDS = listOf("set", "center", "enter")

    private fun firstLevelArgs(args: Array<out String>): List<String>?{


        return when (args[0]) {
            "create" -> null
            else -> return FIRST_LEVEL_SUB_COMMANDS.filter { it.startsWith(args[0]) }.takeIf { it.isNotEmpty() }
        }
    }

    private fun secondLevelArgs(args: Array<out String>): List<String>?{
        val arenaNames = ArenaHelper.ArenaConfigManager.getArenaList().map { it.arenaName }

        return when (args[0]) {
            "spawnpoint" -> SECOND_LEVEL_SPAWNPOINT_SUB_COMMANDS.filter { it.startsWith(args[1]) }.takeIf { it.isNotEmpty() }
            "world" -> SECOND_LEVEL_WORLD_SUB_COMMANDS.filter { it.startsWith(args[1]) }.takeIf { it.isNotEmpty() }
            "create" -> null
            else -> arenaNames.filter { it.startsWith(args[1]) }.takeIf { it.isNotEmpty() }
        }
    }
}