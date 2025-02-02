package gg.ninjagaming.minigamehelpers.commonTables

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * Represents the GameModeConfigurationTable for managing game mode configurations within the database.
 * Maps each column in the "ConfigurationIndex" table to its respective property in the
 * `GameModeConfigurationEntry` interface.
 *
 * The table is intended to store configuration data for various game modes, including details such as
 * identifiers, descriptions, player limits, and state relevant for gameplay logic.
 *
 * Table Columns:
 * - `entryId`: An Integer serving as the primary key, uniquely identifying each game mode configuration entry.
 * - `entryName`: A String representing the name of the game mode configuration.
 * - `entryDescription`: A String providing a description of the game mode configuration.
 * - `defaultLobby`: A String specifying the default lobby associated with the game mode.
 * - `isEnabled`: An Integer flag indicating whether the game mode configuration is enabled (1) or disabled (0).
 * - `minimumPlayers`: An Integer denoting the minimum number of players required for the game mode.
 * - `maximumPlayers`: An Integer representing the maximum number of players allowed for the game mode.
 */
object GameModeConfigurationTable: Table<GameModeConfigurationEntry>("ConfigurationIndex") {
    var entryId = int("entryId").primaryKey().bindTo { it.entryId }
    var entryName = varchar("entryName").bindTo { it.entryName }
    var entryDescription = varchar("entryDescription").bindTo { it.entryDescription }
    var defaultLobby = varchar("defaultLobby").bindTo { it.defaultLobby }
    var isEnabled = int("isEnabled").bindTo { it.isEnabled }
    val minimumPlayers = int("minimumPlayers").bindTo { it.minimumPlayers }
    val maximumPlayers = int("maximumPlayers").bindTo { it.maximumPlayers }
}

/**
 * Represents a configuration entry for a game mode.
 *
 * This interface is used to manage and store game mode-specific settings in the database.
 * Each instance corresponds to a specific game mode's configuration, including its
 * identifying attributes, descriptive information, player limits, and state.
 *
 * Properties:
 * @property entryId Unique identifier for the game mode configuration entry.
 * @property entryName Name of the game mode configuration.
 * @property entryDescription Detailed description of the game mode configuration.
 * @property defaultLobby Name of the default lobby associated with this game mode.
 * @property isEnabled State indicating whether the game mode configuration is enabled.
 * @property minimumPlayers Minimum number of players required to start the game mode.
 * @property maximumPlayers Maximum number of players allowed in the game mode.
 */
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