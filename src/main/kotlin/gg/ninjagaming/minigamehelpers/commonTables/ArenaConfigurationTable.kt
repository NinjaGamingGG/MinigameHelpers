package gg.ninjagaming.minigamehelpers.commonTables

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * Represents the ArenaConfigurationTable for managing arena configurations in the database.
 * Maps each column in the "ArenaConfigurationIndex" table to its respective property in the
 * `ArenaConfigurationEntry` interface.
 *
 * Table Columns:
 * - `entryId`: An Integer representing the unique identifier of the entry, serving as the primary key.
 * - `arenaName`: A String representing the name of the arena.
 * - `arenaDescription`: A String describing the arena.
 * - `arenaWorld`: A String indicating the world associated with the arena.
 * - `arenaCenter`: A String storing the coordinates of the arena's center.
 * - `arenaEnabled`: An Integer used as a flag to determine if the arena is enabled (1) or not (0).
 * - `arenaIcon`: A String representing the icon associated with the arena.
 */
object ArenaConfigurationTable : Table<ArenaConfigurationEntry>("ArenaConfigurationIndex"){
    val entryId = int("entryId").primaryKey().bindTo { it.entryId }
    val arenaName = varchar("arenaName").bindTo { it.arenaName }
    val arenaDescription = varchar("arenaDescription").bindTo { it.arenaDescription }
    val arenaWorld = varchar("arenaWorld").bindTo { it.arenaWorld }
    val arenaCenter = varchar("arenaCenter").bindTo { it.arenaCenter }
    val arenaEnabled = int("arenaEnabled").bindTo { it.arenaEnabled }
    val arenaIcon = varchar("arenaIcon").bindTo { it.arenaIcon }

}

/**
 * Represents a configuration entry for an arena.
 *
 * This interface is part of the data model for storing and managing arena-related configuration
 * within the minigame framework. Each instance corresponds to a specific arena's settings, such
 * as its name, description, world, location, status, and associated icon.
 *
 * Properties:
 * @property entryId Unique identifier for the arena configuration entry.
 * @property arenaName Name of the arena.
 * @property arenaDescription Detailed description of the arena.
 * @property arenaWorld Identifier for the world in which the arena is located.
 * @property arenaCenter Coordinates representing the center location of the arena.
 * @property arenaEnabled State indicating whether the arena is enabled.
 * @property arenaIcon The icon used to visually represent the arena, typically in command UIs.
 */
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