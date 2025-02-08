package gg.ninjagaming.minigamehelpers.commonHelpers

import org.bukkit.Bukkit
import org.bukkit.scoreboard.Scoreboard

/**
 * Helper object for managing and creating Scoreboard instances in Minecraft.
 */
object ScoreboardHelper {
    /**
     * A scoreboard instance that persists across sessions or game states.
     * This variable is initialized at a later point in time and is expected to
     * be assigned a non-null value before being accessed. Its lifecycle ensures
     * that changes to the scoreboard are retained appropriately.
     */
    private var scoreboardMap = HashMap<String, Scoreboard>()

    /**
     * Retrieves the persistent scoreboard instance used in the application.
     * If the scoreboard is not yet initialized, it creates a new scoreboard
     * using the Bukkit ScoreboardManager and assigns it to the persistent variable.
     *
     * @return the persistent Scoreboard instance
     */
    fun getScoreboard(scoreBoardHandle: String): Scoreboard {
        if (!scoreboardMap.containsKey(scoreBoardHandle)) {
            val scoreboardManager = Bukkit.getScoreboardManager()

            val scoreboard = scoreboardManager.newScoreboard
            scoreboard.resetScores("gameInfo")

            scoreboardMap[scoreBoardHandle] = scoreboard
        }


        return scoreboardMap[scoreBoardHandle]!!
    }
}