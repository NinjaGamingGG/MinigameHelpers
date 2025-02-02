package gg.ninjagaming.minigamehelpers.commonCommands.arena.world

import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationTable
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.ktorm.dsl.eq
import org.ktorm.dsl.update

object ArenaWorldSetCenterSubcommand {
    fun setCenter(arenaName: String, x: String, z: String, sender: CommandSender) : Boolean {
        if (Bukkit.getWorld(arenaName) == null) {
            sender.sendMessage("[OITC] The world $arenaName does not exist")
            return false
        }

        val database = DatabaseHelper.getDatabase()

        val updated = database.update(ArenaConfigurationTable) {
            set(it.arenaCenter, "$x,$z")
            where {
                it.arenaName eq arenaName
            }
        }

        if (updated == 0) {
            sender.sendMessage("[OITC] Failed to set center for arena $arenaName")
            return false
        }

        sender.sendMessage("[OITC] Successfully set center for arena $arenaName")

        return true
    }
}