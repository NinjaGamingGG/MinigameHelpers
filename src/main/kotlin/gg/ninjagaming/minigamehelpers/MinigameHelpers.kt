package gg.ninjagaming.minigamehelpers

import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper
import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationEntry
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationTable
import gg.ninjagaming.minigamehelpers.commonTables.ConfigArenaLinkTable
import gg.ninjagaming.minigamehelpers.commonTables.GameModeConfigurationEntry
import gg.ninjagaming.minigamehelpers.commonTables.GameModeConfigurationTable
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.entity.filter
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.forEach
import org.ktorm.entity.map
import org.ktorm.entity.sequenceOf

/**
 * The `MinigameHelpers` object provides utility methods and configuration management for minigame-related functionality.
 * It is designed to handle initialization, configuration loading, and providing access to the plugin's main instance
 * and configuration.
 */
object MinigameHelpers {
    /**
     * Holds the active instance of the `JavaPlugin` class used by the application.
     * This variable is initialized during plugin setup and provides access to the Bukkit
     * plugin's functionality, such as logging and other lifecycle-related actions.
     *
     * The variable is used to manage and interact with the plugin's core operations,
     * including initialization and configuration loading processes.
     */
    private lateinit var pluginInstance: JavaPlugin
    /**
     * Holds the configuration for the plugin, typically loaded from a `.yml` file and used during runtime
     * to retrieve settings and properties. This variable is initialized during the plugin initialization
     * process and provides access across the plugin for configuration-related data.
     */
    private lateinit var pluginConfig: FileConfiguration
    /**
     * Holds the configuration entry for the currently active minigame mode.
     * This property is initialized by loading the corresponding configuration
     * from the database or creating a default entry if none exists.
     *
     * The configuration entry contains details such as:
     * - Name and description of the game mode.
     * - Default lobby associated with the game mode.
     * - Game mode's enabled/disabled status.
     * - Minimum and maximum allowed players.
     *
     * This property is used to retrieve and handle the settings for the active minigame mode,
     * ensuring the required parameters are met for functionality.
     */
    private lateinit var minigameConfig: GameModeConfigurationEntry

    /**
     * Retrieves the current configuration for the minigame.
     *
     * This configuration contains details such as the minimum
     * and maximum number of players required to start the game,
     * as well as other parameters related to the game's settings.
     *
     * @return The minigame configuration object used to manage the game's settings.
     */
    fun getMinigameConfig() = minigameConfig
    /**
     * Retrieves the plugin configuration instance.
     *
     * @return The current plugin configuration.
     */
    fun getPluginConfig() = pluginConfig
    /**
     * Retrieves the singleton instance of the plugin.
     *
     * This function provides access to the main plugin instance, which can be
     * used to interact with the plugin's resources, logger, or other shared states.
     *
     * @return The plugin instance.
     */
    fun getPluginInstance() = pluginInstance



    /**
     * Sets the configuration for the current minigame.
     *
     * This function assigns the provided game mode configuration entry to the minigame.
     * The configuration entry contains settings such as the default lobby, player limits,
     * and whether the game mode is enabled.
     *
     * @param config The game mode configuration entry to be applied to the minigame.
     */
    fun setMinigameConfig(config: GameModeConfigurationEntry) {
        minigameConfig = config
    }

    /**
     * Initializes the helper by setting the plugin instance and configuration.
     * This method also loads the game mode configuration into memory.
     *
     * @param plugin The instance of the JavaPlugin to be used by the helper.
     * @param config The configuration file to be used for initializing the helper.
     */
    fun initHelper(plugin: JavaPlugin, config: FileConfiguration) {
        pluginInstance = plugin
        pluginConfig = config

        DatabaseHelper.connectMysql(config)
        DatabaseHelper.initTables(arrayOf())
        loadGamemodeConfig()
    }

    /**
     * Loads the game mode configuration from the database or creates a default configuration
     * if none exists. It ensures the configuration is properly initialized and stored in memory.
     *
     * If a configuration is specified in the plugin configuration file, it attempts to load it
     * from the database. If it does not exist, a new default configuration is created and inserted
     * into the database.
     *
     * The method is responsible for setting the active game mode configuration and logging
     * any relevant messages related to loading or creation of configurations. Additionally,
     * it initializes the list of enabled arenas using `ArenaHelper.ArenaConfigManager`.
     *
     * Logging is provided for the following events:
     * - A new default configuration is created.
     * - A configuration is successfully loaded.
     * - The gamemode is disabled.
     * - A failure to create the default configuration.
     *
     * This method establishes a connection to the database and directly interacts with the tables:
     * - `GameModeConfigurationTable` to fetch or insert game mode configurations.
     * - `ArenaConfigurationTable` to load a list of active arenas.
     */
    private fun loadGamemodeConfig(){
        val database = DatabaseHelper.getDatabase()

        val entryId = pluginConfig.getString("gamemode.defaultConfig")?: "default"
        var entry: GameModeConfigurationEntry? = null

        entry = database.sequenceOf(GameModeConfigurationTable)
            .filter {GameModeConfigurationTable.entryName eq entryId }
            .firstOrNull()

        if (entry == null){
            val newEntry = GameModeConfigurationEntry{entryName = "default"; entryDescription = "Default config."; defaultLobby = "lobby"; isEnabled = 1; minimumPlayers = 2; maximumPlayers = 8}

            val inserted = database.insert(GameModeConfigurationTable) {
                set(it.entryName, newEntry.entryName)
                set(it.entryDescription, newEntry.entryDescription)
                set(it.defaultLobby, newEntry.defaultLobby)
                set(it.isEnabled, newEntry.isEnabled)
                set(it.minimumPlayers, newEntry.minimumPlayers)
                set(it.maximumPlayers, newEntry.maximumPlayers)
            }


            if(inserted == 0){
                pluginInstance.logger.warning("Failed to create default config.")
                return
            }

            minigameConfig = newEntry

        }else{
            minigameConfig = entry
            pluginInstance.logger.info("Loaded config: ${entry.entryName}")
        }

        if (minigameConfig.isEnabled == 0){
            pluginInstance.logger.warning("The gamemode configuration ${minigameConfig.entryName} is disabled.")
            return
        }

        val arenas = database.sequenceOf(ArenaConfigurationTable)
            .filter {ArenaConfigurationTable.arenaEnabled eq 1}

        val linkedArenaIds = database.sequenceOf(ConfigArenaLinkTable).filter { ConfigArenaLinkTable.configId eq minigameConfig.entryId }.map { it.arenaId }

        if (linkedArenaIds.isEmpty()) {
            pluginInstance.logger.warning("The gamemode configuration ${minigameConfig.entryName} has no arenas linked.")
            return
        }

        var validArenas: List<ArenaConfigurationEntry>? = null

        arenas.forEach {
            if (!linkedArenaIds.contains(it.entryId))
                return@forEach

            validArenas = validArenas?.plus(it) ?: listOf(it)
        }

        if (validArenas == null) {
            pluginInstance.logger.warning("The gamemode configuration ${minigameConfig.entryName} has no valid arenas linked.")
            return
        }

        ArenaHelper.ArenaConfigManager.setArenaList(validArenas)
    }
}