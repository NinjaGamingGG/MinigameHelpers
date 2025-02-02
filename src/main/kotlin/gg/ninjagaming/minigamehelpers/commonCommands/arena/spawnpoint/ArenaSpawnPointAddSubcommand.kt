package gg.ninjagaming.minigamehelpers.commonCommands.arena.spawnpoint

import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationTable
import gg.ninjagaming.minigamehelpers.commonTables.PlayerSpawnPointTable
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.entity.filter
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf

object ArenaSpawnPointAddSubcommand{
    fun addSpawnPoint(sender: CommandSender) :Boolean {
        if (sender !is Player) {
            sender.sendMessage("You must be a player to run this command.")
            return false
        }

        val worldName = sender.world.name

        val database = DatabaseHelper.getDatabase()

        val entry =  database.sequenceOf(ArenaConfigurationTable)
            .filter { ArenaConfigurationTable.arenaWorld eq worldName }
            .firstOrNull()

        if (entry == null) {
            sender.sendMessage("There are no arenas in this world.")
            return false
        }

        val inserted = database.insert(PlayerSpawnPointTable) {
            set(it.arenaName, entry.arenaName)
            set(it.x, sender.location.x)
            set(it.y, sender.location.y)
            set(it.z, sender.location.z)
            set(it.yaw, sender.location.yaw)
            set(it.pitch, sender.location.pitch)
            set(it.isEnabled, 1)
        }

        if(inserted == 0){
            sender.sendMessage("[OITC] Failed to add spawn point.")
            return false
        }

        sender.sendMessage("[OITC] Successfully added spawn point.")

        return true

    }
}