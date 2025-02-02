package gg.ninjagaming.minigamehelpers.commonCommands.arena.spawnpoint

import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.PlayerSpawnPointTable
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq

object ArenaSpawnPointRemoveSubCommand {
    fun removeSpawnPoint(spawnId: Int,sender: CommandSender) :Boolean {
        if (sender !is Player){
            sender.sendMessage("You must be a player to run this command.")
            return false
        }

        val database = DatabaseHelper.getDatabase()

        val deleted =  database.delete(PlayerSpawnPointTable){
            it.id eq spawnId
        }

        if(deleted == 0)
        {
            sender.sendMessage("[OITC] Failed to remove the spawn point.")
            return false
        }

        sender.sendMessage("[OITC] Successfully removed the spawn point.")
        return true
    }
}