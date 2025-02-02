package gg.ninjagaming.minigamehelpers.commonHelpers

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

object ArenaHelper {

    public lateinit var logger: Logger
    public lateinit var config: FileConfiguration


    object PlayerManagement{
        private var playerList = mutableListOf<Player>()

        private var spectatorList = mutableListOf<Player>()

        fun addPlayer(player: Player){
            playerList.add(player)
        }

        fun removePlayer(player: Player){
            playerList.remove(player)
        }

        fun getPlayerList(): MutableList<Player>{
            return playerList
        }

        fun addSpectator(player: Player){
            spectatorList.add(player)
        }

        fun removeSpectator(player: Player){
            spectatorList.remove(player)
        }

        fun getSpectatorList(): MutableList<Player>{
            return spectatorList
        }
    }

    object VoteManager{
        private val arenaVotes: HashMap<Player, String> = HashMap()

        fun addVote(player: Player, arena: String){
            arenaVotes[player] = arena
        }

        fun getVotes(): HashMap<Player, String> {
            return arenaVotes
        }

        fun removeVote(player: Player){
            arenaVotes.remove(player)
        }

    }

    object ArenaConfigManager{
        private lateinit var selectedArenaConfig : ArenaConfigurationEntry
        private lateinit var arenaList: List<ArenaConfigurationEntry>

        fun getArenaList(): List<ArenaConfigurationEntry>{
            return arenaList
        }

        fun setArenaList(arenaList: List<ArenaConfigurationEntry>){
            this.arenaList = arenaList
        }

        fun setSelectedArenaConfig(selectedArenaConfig: ArenaConfigurationEntry){
            this.selectedArenaConfig = selectedArenaConfig
        }

        fun getSelectedArenaConfig(): ArenaConfigurationEntry?{
            if(!::selectedArenaConfig.isInitialized){
                return null
            }
            return selectedArenaConfig
        }
    }

    fun prepareWorld(selectedArena: ArenaConfigurationEntry): World?{

        if (!File("./${selectedArena.arenaWorld}").exists())
        {
            logger.severe("The world $selectedArena.arenaWorld does not exist")
            return null
        }

        val newWorld = Bukkit.createWorld(WorldCreator(selectedArena.arenaWorld))

        Bukkit.getWorlds().add(newWorld)

        val (arenaCenterX, arenaCenterZ) = selectedArena.arenaCenter.split(",").map { it.trim().toDouble() }

        val arenaCenterY = newWorld!!.getHighestBlockAt(arenaCenterX.toInt(),arenaCenterZ.toInt()).location.blockY.toDouble()
        newWorld.spawnLocation = Location(newWorld,arenaCenterX,arenaCenterY,arenaCenterZ)

        logger.info("Loading world ${selectedArena.arenaWorld}...")

        return newWorld
    }

    fun playerLobbyTeleport(player: Player){
        val lobbyX = config.getDouble("lobby.SpawnX")
        val lobbyY = config.getDouble("lobby.SpawnY")
        val lobbyZ = config.getDouble("lobby.SpawnZ")

        val lobbyYaw = config.getDouble("lobby.SpawnYaw")
        val lobbyPitch = config.getDouble("lobby.SpawnPitch")

        val lobbyWorldName = config.getString("lobby.world")

        if (lobbyWorldName == null) {
            logger.log(
                Level.SEVERE,
                "Could not find lobby.world in config.yml")
            return
        }

        val lobbyWorld = Bukkit.getServer().getWorld(lobbyWorldName)

        if (lobbyWorld == null) {
            logger.log(
                Level.SEVERE,
                "Could not find world $lobbyWorldName in config.yml")

            return
        }

        player.teleport(Location(lobbyWorld,lobbyX, lobbyY, lobbyZ, lobbyYaw.toFloat(), lobbyPitch.toFloat()))
    }

    fun prepareArena()
    {
        val selectedArena = ArenaConfigManager.getSelectedArenaConfig()
        if (selectedArena == null) {
            logger.severe("There is no selected arena.")
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