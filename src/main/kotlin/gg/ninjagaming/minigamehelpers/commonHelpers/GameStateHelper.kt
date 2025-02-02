package gg.ninjagaming.minigamehelpers.commonHelpers

object GameStateHelper {
    private var state = GameState.INIT
    private val listeners = mutableListOf<GameStateChangeListener>()


    fun getGameState(): GameState{
        return state
    }

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

    fun addListener(listener: GameStateChangeListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: GameStateChangeListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners(oldState: GameState, newState: GameState) {
        listeners.forEach { listener ->
            listener.onGameStateChange(oldState, newState)
        }
    }

}

enum class GameState{
    INIT,
    LOBBY,
    PREPARING,
    PLAYING,
    ENDING
}


fun interface GameStateChangeListener {
    fun onGameStateChange(oldState: GameState, newState: GameState)
}

