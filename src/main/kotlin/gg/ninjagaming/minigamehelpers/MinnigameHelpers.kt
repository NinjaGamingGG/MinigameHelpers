package gg.ninjagaming.minigamehelpers

import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper
import gg.ninjagaming.minigamehelpers.commonHelpers.DatabaseHelper
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationTable
import gg.ninjagaming.minigamehelpers.commonTables.GameModeConfigurationEntry
import gg.ninjagaming.minigamehelpers.commonTables.GameModeConfigurationTable
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.entity.filter
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList

object MinnigameHelpers {
    private lateinit var pluginInstance: JavaPlugin
    private lateinit var pluginConfig: FileConfiguration
    private lateinit var minigameConfig: GameModeConfigurationEntry

    fun getMinigameConfig() = minigameConfig
    fun getPluginConfig() = pluginConfig
    fun getPluginInstance() = pluginInstance

    fun setPluginInstance(instance: JavaPlugin) {
        pluginInstance = instance
    }

    fun setPluginConfig(config: FileConfiguration) {
        pluginConfig = config
    }

    fun setMinigameConfig(config: GameModeConfigurationEntry) {
        minigameConfig = config
    }

    fun initHelper(plugin: JavaPlugin, config: FileConfiguration) {
        setPluginInstance(plugin)
        setPluginConfig(config)

        loadGamemodeConfig()
    }

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
            pluginInstance.logger.warning("The gamemode ${minigameConfig.entryName} is disabled.")
            return
        }

        val arenas = database.sequenceOf(ArenaConfigurationTable)
            .filter {ArenaConfigurationTable.arenaEnabled eq 1}

        ArenaHelper.ArenaConfigManager.setArenaList(arenas.toList())
    }
}