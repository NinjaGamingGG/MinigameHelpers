package gg.ninjagaming.minigamehelpers.commonTables

import org.ktorm.entity.Entity
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object ArenaConfigurationTable : Table<ArenaConfigurationEntry>("ArenaConfigurationIndex"){
    val entryId = int("entryId").primaryKey().bindTo { it.entryId }
    val arenaName = varchar("arenaName").bindTo { it.arenaName }
    val arenaDescription = varchar("arenaDescription").bindTo { it.arenaDescription }
    val arenaWorld = varchar("arenaWorld").bindTo { it.arenaWorld }
    val arenaCenter = varchar("arenaCenter").bindTo { it.arenaCenter }
    val arenaEnabled = int("arenaEnabled").bindTo { it.arenaEnabled }
    val arenaIcon = varchar("arenaIcon").bindTo { it.arenaIcon }

}

interface ArenaConfigurationEntry: Entity<ArenaConfigurationEntry> {
    companion object : Entity.Factory<ArenaConfigurationEntry>()
    val entryId: Int
    var arenaName: String
    var arenaDescription: String
    var arenaWorld: String
    var arenaCenter: String
    var arenaEnabled: Int
    var arenaIcon: String
}