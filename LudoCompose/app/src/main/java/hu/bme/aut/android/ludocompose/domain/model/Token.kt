package hu.bme.aut.android.ludocompose.domain.model

data class Token(
    var state: State = State.YARD,
    var trackPos: Int = 0
) {
    enum class State {
        YARD,
        TRACK,
        HOME,
    }
}
