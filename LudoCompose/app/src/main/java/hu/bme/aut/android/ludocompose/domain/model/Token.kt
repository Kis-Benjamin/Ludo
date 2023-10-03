package hu.bme.aut.android.ludocompose.domain.model

class Token(
    var state: State = State.YARD,
    var trackPos: Int = 0
) {
    val isInYard: Boolean get() = state == State.YARD

    val isInTrack: Boolean get() = state == State.TRACK

    val isInHome: Boolean get() = state == State.HOME

    fun steppedOverExit(color: Int) = trackPos >= 40 + 10 * color

    fun isValidStep(dice: Int) = when (state) {
        State.YARD -> dice == 6
        State.TRACK -> true
        State.HOME -> false
    }

    private fun enterTrack(color: Int) {
        state = State.TRACK
        trackPos = 10 * color
    }

    private fun advance(color: Int, dice: Int) {
        trackPos += dice
        if (steppedOverExit(color)) enterHome()
    }

    private fun enterHome() {
        state = State.HOME
        trackPos = 0
    }

    fun executeStep(color: Int, dice: Int): Boolean {
        when (state) {
            State.YARD -> if (dice == 6) enterTrack(color)
            State.TRACK -> advance(color, dice)
            State.HOME -> return false
        }
        return isInHome
    }

    enum class State {
        YARD,
        TRACK,
        HOME,
    }
}
