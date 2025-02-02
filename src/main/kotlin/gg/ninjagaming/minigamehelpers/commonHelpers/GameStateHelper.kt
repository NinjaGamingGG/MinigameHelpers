package gg.ninjagaming.minigamehelpers.commonHelpers

/**
 * Utility object for managing the state of a game and notifying listeners of state transitions.
 */
object GameStateHelper {
    private var state = GameState.INIT
    private val listeners = mutableListOf<GameStateChangeListener>()


    /**
     * Retrieves the current state of the game.
     *
     * @return The current state of the game as a GameState enum value.
     * Possible values are INIT, LOBBY, PREPARING, PLAYING, and ENDING.
     */
    fun getGameState(): GameState{
        return state
    }

    /**
     * Advances the current game state to the next sequential state.
     *
     * The state progression follows a predefined order:
     * - INIT -> LOBBY
     * - LOBBY -> PREPARING
     * - PREPARING -> PLAYING
     * - PLAYING -> ENDING
     * - ENDING -> ENDING (No further transition)
     *
     * This method updates the state, and if a state change occurs, it notifies all registered listeners
     * by calling the `notifyListeners` method with the previous and the new game state.
     */
    fun advanceState(){
        val oldState = state
        when(state){
            GameState.INIT -> {
                state = GameState.LOBBY
            }
            GameState.LOBBY -> {
                state = GameState.PREPARING
            }
            GameState.PREPARING ->{
                state =  GameState.PLAYING
            }
            GameState.PLAYING -> {
                state = GameState.ENDING
            }
            GameState.ENDING -> {
                state = GameState.ENDING
            }
        }

        if (oldState != state) {
            notifyListeners(oldState, state)
        }

    }

    /**
     * Adds a listener to monitor changes in the game state.
     *
     * @param listener An implementation of the GameStateChangeListener interface that will handle
     *                 game state change events. This listener is added to the list of active listeners.
     */
    fun addListener(listener: GameStateChangeListener) {
        listeners.add(listener)
    }

    /**
     * Removes a previously registered listener from the list of listeners.
     *
     * @param listener The instance of GameStateChangeListener to be removed from the listeners list.
     */
    fun removeListener(listener: GameStateChangeListener) {
        listeners.remove(listener)
    }

    /**
     * Notifies all registered listeners about a change in the game state.
     *
     * @param oldState The previous state of the game.
     * @param newState The current state of the game.
     */
    private fun notifyListeners(oldState: GameState, newState: GameState) {
        listeners.forEach { listener ->
            listener.onGameStateChange(oldState, newState)
        }
    }

}

/**
 * Represents the various states a game can transition through during its lifecycle.
 *
 * Enum entries:
 * - INIT: The initial state when the game is being set up.
 * - LOBBY: The state where players are in the lobby, waiting for the game to start.
 * - PREPARING: The state where preparations for the game are being made, such as loading the arena or setting up resources.
 * - PLAYING: The active state where the game is in progress.
 * - ENDING: The final state where the game is finishing, including post-game events and cleanup.
 */
enum class GameState{
    INIT,
    LOBBY,
    PREPARING,
    PLAYING,
    ENDING
}


/**
 * Represents a listener for changes in the state of a game.
 * Classes or objects implementing this functional interface can react to
 * transitions between different game states.
 *
 * The `GameStateChangeListener` is triggered when the state of the game changes
 * and provides both the previous and the new state of the game.
 *
 * Intended use cases might include updating UI elements,
 * managing game mechanics corresponding to the game state, or notifying players.
 *
 * @param oldState The previous state of the game before the transition.
 * @param newState The new state of the game after the transition.
 */
fun interface GameStateChangeListener {
    fun onGameStateChange(oldState: GameState, newState: GameState)
}

