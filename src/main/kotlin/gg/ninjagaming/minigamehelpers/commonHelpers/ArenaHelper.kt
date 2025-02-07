package gg.ninjagaming.minigamehelpers.commonHelpers

import gg.ninjagaming.minigamehelpers.MinigameHelpers
import gg.ninjagaming.minigamehelpers.commonTables.ArenaConfigurationEntry
import gg.ninjagaming.minigamehelpers.commonTables.PlayerSpawnPointTable
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import java.io.File
import java.util.logging.Level
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.collections.set
import kotlin.isInitialized
import kotlin.text.split
import kotlin.text.toDouble
import kotlin.text.trim
import java.util.logging.Logger

/**
 * Object responsible for managing arena-related functions and utilities
 * within a minigame framework. Provides functionality for handling player
 * management, arena configuration, voting, and preparing the game environment.
 *
 * Contains nested objects to group related functionality:
 * - PlayerManagement
 * - VoteManager
 * - ArenaConfigManager
 */
object ArenaHelper {
    /**
     * An object for managing players and spectators within a game.
     * Provides functionality to add, remove, and retrieve both players and spectators.
     */
    object PlayerManagement{
        private var playerList = mutableListOf<Player>()

        private var spectatorList = mutableListOf<Player>()

        /**
         * Adds a player to the player list.
         *
         * @param player The player object to be added to the player list.
         */
        fun addPlayer(player: Player){
            playerList.add(player)
        }

        /**
         * Removes a player from the player list.
         *
         * @param player The player to be removed from the player list.
         */
        fun removePlayer(player: Player){
            playerList.remove(player)
        }

        /**
         * Retrieves the list of players currently associated with the game or application.
         *
         * @return A mutable list of Player objects representing the players.
         */
        fun getPlayerList(): MutableList<Player>{
            return playerList
        }

        /**
         * Adds a player to the spectator list.
         *
         * @param player The player to be added as a spectator.
         */
        fun addSpectator(player: Player){
            spectatorList.add(player)
        }

        /**
         * Removes a player from the spectator list.
         *
         * @param player The Player object to be removed from the spectator list.
         */
        fun removeSpectator(player: Player){
            spectatorList.remove(player)
        }

        /**
         * Retrieves the list of players who are currently spectators.
         *
         * @return A mutable list containing Player objects representing the spectators.
         */
        fun getSpectatorList(): MutableList<Player>{
            return spectatorList
        }
    }

    /**
     * Manages player votes for a specific arena in a game.
     *
     * This object provides functionality to add, retrieve, and remove votes cast by players.
     * Votes are stored in an internal data structure that maps players to the arenas they voted for.
     */
    object VoteManager{
        private val arenaVotes: HashMap<Player, String> = HashMap()

        /**
         * Registers a vote for a player in a specific arena.
         *
         * @param player The player casting the vote.
         * @param arena The name of the arena being voted for.
         */
        fun addVote(player: Player, arena: String){
            arenaVotes[player] = arena
        }

        /**
         * Retrieves a map of players and their respective votes in the current game state.
         *
         * @return A HashMap where each key is a Player object representing a participant,
         *         and each value is a String representing the vote cast by that player.
         */
        fun getVotes(): HashMap<Player, String> {
            return arenaVotes
        }

        /**
         * Removes a player's vote from the arenaVotes data structure.
         *
         * @param player The player whose vote should be removed.
         */
        fun removeVote(player: Player){
            arenaVotes.remove(player)
        }

    }

    /**
     * Manages configurations for arenas, providing functionality to set and retrieve
     * the list of available arena configurations and the selected arena configuration.
     */
    object ArenaConfigManager{
        private lateinit var selectedArenaConfig : ArenaConfigurationEntry
        private lateinit var arenaList: List<ArenaConfigurationEntry>

        /**
         * Retrieves a list of arena configuration entries.
         *
         * This method returns all the configured arenas as a list of `ArenaConfigurationEntry` objects.
         * These entries represent detailed configurations of each arena, including associated metadata and settings.
         *
         * @return A list of `ArenaConfigurationEntry` containing the configurations of all arenas.
         */
        fun getArenaList(): List<ArenaConfigurationEntry>{
            return arenaList
        }

        /**
         * Sets the list of arena configuration entries for this instance.
         *
         * @param arenaList The list of [ArenaConfigurationEntry] objects representing the configuration settings for each arena.
         */
        fun setArenaList(arenaList: List<ArenaConfigurationEntry>){
            this.arenaList = arenaList
        }

        /**
         * Sets the selected arena configuration.
         *
         * This method assigns the provided `ArenaConfigurationEntry` to the selected arena configuration,
         * which can be used in various logic to manage or interact with the configured arena.
         *
         * @param selectedArenaConfig An instance of `ArenaConfigurationEntry` representing
         * the arena configuration to be set as the selected configuration.
         */
        fun setSelectedArenaConfig(selectedArenaConfig: ArenaConfigurationEntry){
            this.selectedArenaConfig = selectedArenaConfig
        }

        /**
         * Retrieves the currently selected arena configuration.
         *
         * This method checks if the `selectedArenaConfig` property has been initialized.
         * If it is not initialized, the method returns null; otherwise, it returns the value of `selectedArenaConfig`.
         *
         * @return The currently selected `ArenaConfigurationEntry` or null if it is not initialized.
         */
        fun getSelectedArenaConfig(): ArenaConfigurationEntry?{
            if(!::selectedArenaConfig.isInitialized){
                return null
            }
            return selectedArenaConfig
        }
    }

    /**
     * Prepares the world for gameplay by loading a world from the specified arena configuration,
     * setting the spawn location, and registering it to the active worlds list.
     *
     * @param selectedArena The arena configuration entry containing the details for the desired world, including its name and center location.
     * @return The loaded and prepared world if the operation is successful, or null if the world does not exist.
     */
    fun prepareWorld(selectedArena: ArenaConfigurationEntry): World?{

        if (!File("./${selectedArena.arenaWorld}").exists())
        {
            MinigameHelpers.getPluginInstance().logger.severe("The world $selectedArena.arenaWorld does not exist")
            return null
        }

        val newWorld = Bukkit.createWorld(WorldCreator(selectedArena.arenaWorld))

        Bukkit.getWorlds().add(newWorld)

        val (arenaCenterX, arenaCenterZ) = selectedArena.arenaCenter.split(",").map { it.trim().toDouble() }

        val arenaCenterY = newWorld!!.getHighestBlockAt(arenaCenterX.toInt(),arenaCenterZ.toInt()).location.blockY.toDouble()
        newWorld.spawnLocation = Location(newWorld,arenaCenterX,arenaCenterY,arenaCenterZ)

        MinigameHelpers.getPluginInstance().logger.info("Loading world ${selectedArena.arenaWorld}...")

        return newWorld
    }

    /**
     * Teleports the specified player to the configured lobby location.
     * The teleportation details such as coordinates, yaw, pitch, and world are retrieved from the plugin's configuration file.
     * Logs a severe error if the required configuration values are missing or invalid.
     *
     * @param player The player to be teleported to the configured lobby location.
     */
    fun playerLobbyTeleport(player: Player){
        val config: FileConfiguration = MinigameHelpers.getPluginConfig()
        val lobbyX = config.getDouble("lobby.SpawnX")
        val lobbyY = config.getDouble("lobby.SpawnY")
        val lobbyZ = config.getDouble("lobby.SpawnZ")

        val lobbyYaw = config.getDouble("lobby.SpawnYaw")
        val lobbyPitch = config.getDouble("lobby.SpawnPitch")

        val lobbyWorldName = config.getString("lobby.world")

        if (lobbyWorldName == null) {
            MinigameHelpers.getPluginInstance().logger.log(
                Level.SEVERE,
                "Could not find lobby.world in config.yml")
            return
        }

        val lobbyWorld = Bukkit.getServer().getWorld(lobbyWorldName)

        if (lobbyWorld == null) {
            MinigameHelpers.getPluginInstance().logger.log(
                Level.SEVERE,
                "Could not find world $lobbyWorldName in config.yml")

            return
        }

        player.teleport(Location(lobbyWorld,lobbyX, lobbyY, lobbyZ, lobbyYaw.toFloat(), lobbyPitch.toFloat()))
    }

    /**
     * Prepares the current arena for gameplay by validating the selected arena configuration
     * and initializing the corresponding world.
     *
     * This method retrieves the currently selected arena configuration using `ArenaConfigManager`.
     * If no arena configuration is selected, an error is logged, and the server is shut down.
     * Otherwise, the arena's world is prepared using the `prepareWorld` function, which loads and configures the world.
     *
     * Function behavior:
     * - Determines the currently selected arena configuration.
     * - Logs an error message and shuts down the server if no arena is selected.
     * - Prepares the game world for the selected arena, including loading the world, setting the spawn location,
     *   and initializing any additional required resources.
     *
     * Note: Ensure the selected arena is properly configured, and its associated world files are available
     * to avoid errors during the preparation process.
     */
    fun prepareArena()
    {
        val selectedArena = ArenaConfigManager.getSelectedArenaConfig()
        if (selectedArena == null) {
            MinigameHelpers.getPluginInstance().logger.severe("There is no selected arena.")
            Bukkit.getServer().shutdown()
            return
        }
        prepareWorld(selectedArena)
    }

    private lateinit var spawnPointsList: MutableList<Location>

    fun getRespawnPoints(): List<Location>{
        if (::spawnPointsList.isInitialized && spawnPointsList.isNotEmpty())
            return spawnPointsList

        val database = DatabaseHelper.getDatabase()

        if (ArenaConfigManager.getSelectedArenaConfig() == null) {
            return emptyList()
        }

        val resultList = database.sequenceOf(PlayerSpawnPointTable).filter { it.arenaName eq ArenaConfigManager.getSelectedArenaConfig()!!.arenaName }.toList()

        val arenaWorld = Bukkit.getWorld(ArenaConfigManager.getSelectedArenaConfig()!!.arenaWorld)

        val newSpawnPointsList = mutableListOf<Location>()

        resultList.forEach {
            newSpawnPointsList.add(Location(arenaWorld, it.x, it.y, it.z, it.yaw, it.pitch))
        }

        spawnPointsList = newSpawnPointsList

        return spawnPointsList
    }
}