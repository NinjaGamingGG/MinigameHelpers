package gg.ninjagaming.minigamehelpers.commonRunnables

import gg.ninjagaming.minigamehelpers.commonHelpers.ArenaHelper
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title.Times.times
import net.kyori.adventure.title.Title.title
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.Duration
import kotlin.collections.forEach

object PostGameRunnable {
    fun scheduleRunnable(winningPlayer: Player?, waitingTime: Int): Runnable {
        countdown = waitingTime

        val runnable = Runnable {
            mainGameTick(winningPlayer)
        }



        return runnable

    }

    var countdown = 0
    fun mainGameTick(winningPlayer: Player?){
        if (countdown == 0)
            Bukkit.shutdown()


        var mainTitle = Component.text("No player has won the game.")
        if (winningPlayer != null)
            mainTitle = Component.text("Player §6${winningPlayer.name}§r has won the game!")

        val winnerTitle = title(mainTitle, Component.text("§oServer Stops in $countdown seconds"), times(Duration.ZERO, Duration.ofSeconds(3), Duration.ZERO))
        val players = ArenaHelper.PlayerManagement.getPlayerList()
        players.forEach {
            it.showTitle(winnerTitle)
        }

        countdown--
    }
}