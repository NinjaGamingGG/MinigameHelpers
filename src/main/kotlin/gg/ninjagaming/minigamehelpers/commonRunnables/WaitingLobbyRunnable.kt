package gg.ninjagaming.minigamehelpers.commonRunnables

import gg.ninjagaming.minigamehelpers.MinigameHelpers
import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper
import gg.ninjagaming.minigamehelpers.commonHelpers.GameStateHelper
import gg.ninjagaming.minigamehelpers.commonHelpers.ScoreboardHelper
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.Times.times
import net.kyori.adventure.title.Title.title
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.scheduler.BukkitTask
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import java.time.Duration
import kotlin.collections.count
import kotlin.collections.find
import kotlin.collections.forEach
import kotlin.collections.maxByOrNull
import kotlin.collections.random
private val waitingForPlayersTitle = title(Component.text("Waiting for Players..."), Component.text("§e${MinigameHelpers.getMinigameConfig().minimumPlayers} §rPlayers required to Start"),times(Duration.ofSeconds(0), Duration.ofSeconds(3),Duration.ofSeconds(0)))
private fun countdownTitle(duration: Int): Title {
    var subtitleComponent = Component.text("§oVote now for a Arena!")
    if (duration < 5){
        subtitleComponent = Component.text("Selected Arena: §2${ArenaHelper.ArenaConfigManager.getSelectedArenaConfig()?.arenaName}")

    }

    return title(Component.text("Starting in §e$duration §rseconds..."), subtitleComponent, times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(0)))
}

object WaitingLobbyRunnable {
    fun scheduleRunnable(): Runnable {
        val runnable = Runnable {
            waitingLobbyTick()
        }
        return runnable
    }


    fun waitingLobbyTick() {
        val playerList = ArenaHelper.PlayerManagement.getPlayerList()
        val gameModeConfig = MinigameHelpers.getMinigameConfig()

        //Check if enough players are in the Lobby to start the Countdown
        if (playerList.count() >= gameModeConfig.minimumPlayers) {
            if (!WaitingLobbyCountdownTimer.isRunning() && WaitingLobbyCountdownTimer.getDuration() != 0) {
                WaitingLobbyCountdownTimer.startTimer()
                return
            }

        }//If there aren't enough players anymore but countdown is already started we stop it. If the Countdown has reached its final phase this is ignored
        else{
            if (WaitingLobbyCountdownTimer.isRunning() && WaitingLobbyCountdownTimer.getDuration() > MinigameHelpers.getPluginConfig().getInt("lobby.Countdown_Final_Phase_Seconds")){
                WaitingLobbyCountdownTimer.stopTimer()
                ArenaHelper.PlayerManagement.getPlayerList().forEach {
                    it.sendMessage("Countdown stopped, §c${gameModeConfig.minimumPlayers} §rplayers required to start the game.")
                    it.playSound(it.location, Sound.ENTITY_VILLAGER_NO, 0.5f, 1f)
                }
                return
            }
        }

        //While countdown isn't running or elapsed display Title to Player
        if(!WaitingLobbyCountdownTimer.isRunning() && WaitingLobbyCountdownTimer.getDuration() != 0){
            ArenaHelper.PlayerManagement.getPlayerList().forEach {
                it.showTitle(waitingForPlayersTitle)
            }
        }

        //if Countdown has elapsed stop the Lobby runnable which advances the Game-State to the next level
        if (!WaitingLobbyCountdownTimer.isRunning() && WaitingLobbyCountdownTimer.getDuration() == 0) {
            GameStateHelper.advanceState()

        }

        playerList.forEach {
            it.scoreboard = buildScoreboard()
        }

    }

    private object WaitingLobbyCountdownTimer {
        private val COUNTDOWN_FINAL_PHASE_SECONDS = MinigameHelpers.getPluginConfig().getInt("lobby.Countdown_Final_Phase_Seconds")
        private val DEFAULT_DURATION = MinigameHelpers.getPluginConfig().getInt("lobby.Countdown_Duration_Seconds") + COUNTDOWN_FINAL_PHASE_SECONDS


        private var timerDuration = DEFAULT_DURATION
        var task: BukkitTask? = null

        fun startTimer() {
            task = Bukkit.getScheduler().runTaskTimer(MinigameHelpers.getPluginInstance(), Runnable {
                if (timerDuration == 0) {
                    task?.cancel()
                    return@Runnable
                }


                //If the Countdown has reached the final phase select the arena and prepare it
                if (timerDuration <= COUNTDOWN_FINAL_PHASE_SECONDS && ArenaHelper.ArenaConfigManager.getSelectedArenaConfig() == null){
                    selectVotedArena()
                    ArenaHelper.prepareArena()
                }

                //Show Countdown title to player and play sound
                ArenaHelper.PlayerManagement.getPlayerList().forEach {
                    it.showTitle(countdownTitle(timerDuration))
                    it.playSound(it.location, Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1f)
                    it.exp = timerDuration.toFloat() / (DEFAULT_DURATION + COUNTDOWN_FINAL_PHASE_SECONDS)
                }

                timerDuration--

            },0L,20L)

            Bukkit.broadcast(Component.text("Player minimum Reached! Game is starting in §2$timerDuration seconds"))
        }

        fun stopTimer() {
            task?.cancel()
            timerDuration = DEFAULT_DURATION
        }

        fun getDuration(): Int {
            return timerDuration
        }

        fun isRunning(): Boolean {
            return task != null && !task!!.isCancelled
        }

        private fun selectVotedArena(){
            val voteList = ArenaHelper.VoteManager.getVotes()

            if(voteList.isEmpty()) {
                selectRandomArena()
                return
            }

            //Return entry with the most votes or null
            val selectedArena = voteList.maxByOrNull { it.value }
            if (selectedArena == null) {
                selectRandomArena()
                return
            }

            val selectedConfig = ArenaHelper.ArenaConfigManager.getArenaList()
                .find { it.arenaName == selectedArena.value }

            if (selectedConfig == null) {
                MinigameHelpers.getPluginInstance().logger.warning("The selected arena ${selectedArena.value} does not exist.")
                selectRandomArena()
                return
            }

            ArenaHelper.ArenaConfigManager.setSelectedArenaConfig(selectedConfig)
            Bukkit.broadcast(Component.text("Arena selected: §2${selectedArena.value}"))


        }

        private fun selectRandomArena(): String{
            val randomArena = ArenaHelper.ArenaConfigManager.getArenaList().random()
            ArenaHelper.ArenaConfigManager.setSelectedArenaConfig(randomArena)

            Bukkit.broadcast(Component.text("No votes received, selected a random arena: §e${randomArena.arenaName}"))

            return randomArena.arenaName
        }
    }

    fun buildScoreboard(): Scoreboard {

        val scoreboard = ScoreboardHelper.getScoreboard()
        var objective = scoreboard.getObjective("gameInfo")

        if (objective == null)
            objective = scoreboard.registerNewObjective("gameInfo", Criteria.DUMMY, Component.text("§6Minigame Info"))

        objective.displaySlot = DisplaySlot.SIDEBAR

        val currentPlayers = ArenaHelper.PlayerManagement.getPlayerList().size
        val minimumPlayers = MinigameHelpers.getMinigameConfig().minimumPlayers
        val maximumPlayers = MinigameHelpers.getMinigameConfig().maximumPlayers

        var playerCountLine = "Players: §e$currentPlayers/$maximumPlayers"
        if (currentPlayers < minimumPlayers)
            playerCountLine = "Required Players: §c$currentPlayers/$minimumPlayers"

            objective.getScore(playerCountLine).score = 9

        var arenaLine = "Arena: §6Vote Now!"
        if (ArenaHelper.ArenaConfigManager.getSelectedArenaConfig() != null)
            arenaLine = "Arena: §6${ArenaHelper.ArenaConfigManager.getSelectedArenaConfig()?.arenaName}"

        objective.getScore(arenaLine).score = 8

        return scoreboard
    }
}