package gg.ninjagaming.minigamehelpers.commonTables

import org.ktorm.entity.Entity
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object GameModeConfigurationTable: Table<GameModeConfigurationEntry>("ConfigurationIndex") {
    var entryId = int("entryId").primaryKey().bindTo { it.entryId }
    var entryName = varchar("entryName").bindTo { it.entryName }
    var entryDescription = varchar("entryDescription").bindTo { it.entryDescription }
    var defaultLobby = varchar("defaultLobby").bindTo { it.defaultLobby }
    var isEnabled = int("isEnabled").bindTo { it.isEnabled }
    val minimumPlayers = int("minimumPlayers").bindTo { it.minimumPlayers }
    val maximumPlayers = int("maximumPlayers").bindTo { it.maximumPlayers }
}

interface GameModeConfigurationEntry: Entity<GameModeConfigurationEntry> {
    companion object : Entity.Factory<GameModeConfigurationEntry>()
    val entryId: Int
    var entryName: String
    var entryDescription: String
    var defaultLobby: String
    var isEnabled: Int
    var minimumPlayers: Int
    var maximumPlayers: Int
}