package gg.ninjagaming.minigamehelpers.commonTables

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object ConfigArenaLinkTable : Table<ConfigArenaLinkEntry>("ConfigArenaLinkIndex"){
    val entryId = int("entryId").primaryKey().bindTo { it.entryId }
    val configId = int("configId").bindTo { it.configId }
    val arenaId = int("arenaId").bindTo { it.arenaId }
}

interface ConfigArenaLinkEntry: Entity<ConfigArenaLinkEntry> {
    companion object : Entity.Factory<ConfigArenaLinkEntry>()
    val entryId: Int
    var configId: Int
    var arenaId: Int
}

val sqlTableString = "CREATE TABLE IF NOT EXISTS ConfigArenaLinkIndex (" +
        "entryId INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
        "configId INT," +
        "arenaId INT)"