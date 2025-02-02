package gg.ninjagaming.minigamehelpers.commonTables

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.double
import org.ktorm.schema.float
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * Represents the PlayerSpawnPointTable for managing player spawn point data within the database.
 * Maps each column in the "PlayerSpawnPointIndex" table to its respective property in the
 * `PlayerSpawnPointEntry` interface.
 *
 * This table is intended to store spawn point information for players within arenas, including
 * coordinates, orientation, and state of the spawn point.
 *
 * Table Columns:
 * - `id`: An Integer representing the unique identifier of the spawn point entry, serving as the primary key.
 * - `arenaName`: A String indicating the name of the associated arena where the spawn point is located.
 * - `x`: A Double value representing the X-coordinate of the spawn point.
 * - `y`: A Double value representing the Y-coordinate of the spawn point.
 * - `z`: A Double value representing the Z-coordinate of the spawn point.
 * - `yaw`: A Float representing the yaw (horizontal angle) of the spawn point's orientation.
 * - `pitch`: A Float representing the pitch (vertical angle) of the spawn point's orientation.
 * - `isEnabled`: An Integer flag indicating whether the spawn point is enabled (1) or disabled (0).
 */
object PlayerSpawnPointTable: Table<PlayerSpawnPointEntry>("PlayerSpawnPointIndex") {
    val id = int("entryId").primaryKey().bindTo { it.id }
    val arenaName = varchar("arenaName").bindTo { it.arenaName }
    val x = double("X").bindTo { it.x }
    val y = double("Y").bindTo { it.y }
    val z = double("Z").bindTo { it.z }
    val yaw = float("YAW").bindTo { it.yaw }
    val pitch = float("PITCH").bindTo { it.pitch }
    val isEnabled = int("isEnabled").bindTo { it.isEnabled }
}

/**
 * Represents a spawn point entry for a player within a specific arena.
 *
 * This interface is used to manage the configuration of spawn points for players in a minigame framework.
 * Each instance corresponds to a spawn location, including its coordinates, orientation, associated arena,
 * and state. These spawn points are critical for defining where players appear within an arena.
 *
 * Properties:
 * @property id Unique identifier for the spawn point entry.
 * @property arenaName Name of the arena associated with this spawn point.
 * @property x X-coordinate of the spawn point's location.
 * @property y Y-coordinate of the spawn point's location.
 * @property z Z-coordinate of the spawn point's location.
 * @property yaw Orientation yaw for the player's facing direction at the spawn point.
 * @property pitch Orientation pitch for the player's facing direction at the spawn point.
 * @property isEnabled State indicating whether the spawn point is enabled (typically 1 for enabled, 0 for disabled).
 */
interface PlayerSpawnPointEntry: Entity<PlayerSpawnPointEntry>{
    companion object : Entity.Factory<PlayerSpawnPointEntry>()
    val id: Int
    var arenaName: String
    var x: Double
    var y: Double
    var z: Double
    var yaw: Float
    var pitch: Float
    var isEnabled: Int
}