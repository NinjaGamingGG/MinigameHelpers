package gg.ninjagaming.minigamehelpers.commonHelpers

import org.bukkit.Bukkit
import org.bukkit.scoreboard.Scoreboard

/**
 * Helper object for managing and creating Scoreboard instances in Minecraft.
 */
object ScoreboardHelper {
    /**
     * Creates and returns a new scoreboard instance while resetting the scores for "gameInfo".
     *
     * @return A new instance of the Scoreboard.
     */
    fun getScoreboard(): Scoreboard {
        val scoreboardManager = Bukkit.getScoreboardManager()

        val scoreboard = scoreboardManager.newScoreboard
        scoreboard.resetScores("gameInfo")

        return scoreboard
    }
}