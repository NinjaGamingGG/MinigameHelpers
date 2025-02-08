package gg.ninjagaming.minigamehelpers.commonHelpers

import gg.ninjagaming.minigamehelpers.MinigameHelpers
import org.bukkit.entity.Player

/**
 * A helper object for sending messages to players in a consistent and formatted manner.
 */
object PlayerMessageHelper {
    /**
     * Sends a formatted message to the specified player.
     *
     * @param player The player to whom the message will be sent.
     * @param message The message content to be sent to the player.
     */
    fun sendMessage(player: Player, message: String){
        player.sendMessage("[${MinigameHelpers.getPluginConfig().get("prefix")}] $message")
    }
}