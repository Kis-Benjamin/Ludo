package hu.bme.aut.android.ludocompose.domain.model

class Token(
    var state: State = State.YARD,
    var trackPos: Int = 0
) {
    val isInYard: Boolean get() = state == State.YARD

    val isInTrack: Boolean get() = state == State.TRACK

    val isInHome: Boolean get() = state == State.HOME

    enum class State {
        YARD,
        TRACK,
        HOME,
    }
}
