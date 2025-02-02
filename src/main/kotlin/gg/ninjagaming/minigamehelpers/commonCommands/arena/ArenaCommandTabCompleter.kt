package gg.ninjagaming.minigamehelpers.commonCommands.arena

import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import kotlin.text.startsWith

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