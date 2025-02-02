package gg.ninjagaming.minigamehelpers.commonCommands.arena.spawnpoint

import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationTable
import gg.ninjagaming.minigamehelpers.commonTables.PlayerSpawnPointTable
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList

object ArenaSpawnPointListSubcommand {
    fun listSpawnPoints(sender: CommandSender) :Boolean {
        if (sender !is Player){
            sender.sendMessage("You must be a player to use this command.")
            return false
        }


        val database = DatabaseHelper.getDatabase()

        val arenaConfig = database.sequenceOf(ArenaConfigurationTable).filter { it.arenaWorld eq sender.world.name }.firstOrNull()

        if (arenaConfig == null) {
            sender.sendMessage("There are no arenas in this world.")
            return false
        }

        val resultList = database.sequenceOf(PlayerSpawnPointTable).filter { it.arenaName eq arenaConfig.arenaName }.toList()

        if (resultList.isEmpty()) {
            sender.sendMessage("There are no spawn points in this world.")
            return false
        }


        var resultString = "There are ${resultList.size} spawn points in this world.\n"
        for (result in resultList) {
            resultString += "----------------------------------------\n"
            resultString += ("SpawnPointId: ${result.id}\n")
            resultString += ("Location: x: ${result.x}, y: ${result.y}, z: ${result.z}\n")
            resultString += ("Yaw: ${result.yaw}, Pitch: ${result.pitch}\n")
            resultString += ("Enabled: ${result.isEnabled}\n")
        }

        sender.sendMessage(resultString)
        return true
    }
}