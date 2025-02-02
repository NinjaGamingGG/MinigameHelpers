package gg.ninjagaming.minigamehelpers.commonHelpers

import java.util.UUID

/**
 * Utility object to manage player scores based on their UUIDs.
 * Provides methods to manipulate and retrieve scores and track the highest score.
 */
object PlayerScoreHelper {
    val scores = HashMap<UUID, Int>()

    /**
     * Increases the score of a player identified by their UUID.
     * If the player does not have an existing score, their score is initialized to 1.
     *
     * @param uuid The unique identifier of the player whose score should be increased.
     */
    fun increaseScore(uuid: UUID){
        scores[uuid] = scores[uuid]?.plus(1) ?: 1
    }

    /**
     * Retrieves the score associated with the given UUID.
     *
     * @param uuid The unique identifier for which the score is to be fetched.
     * @return The score associated with the given UUID, or 0 if no score exists for the UUID.
     */
    fun getScore(uuid: UUID): Int{
        return scores[uuid] ?: 0
    }

    /**
     * Retrieves the highest score from the list of scores.
     *
     * @return The highest score as an integer.
     */
    fun getHighestScore(): Int{
        return scores.maxBy { it.value }.value
    }

    fun getHighestScorePlayer(): UUID{
        return scores.maxBy { it.value }.key
    }
}