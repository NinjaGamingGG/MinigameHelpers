package gg.ninjagaming.minigamehelpers.commonHelpers

import org.bukkit.Bukkit
import org.bukkit.scoreboard.Scoreboard

object ScoreboardHelper {
    fun getScoreboard(): Scoreboard {
        val scoreboardManager = Bukkit.getScoreboardManager()

        val scoreboard = scoreboardManager.newScoreboard
        scoreboard.resetScores("gameInfo")

        return scoreboard
    }
}