package hu.bme.aut.android.ludocompose.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class UiEventViewModel(
    private val coroutineScope: CoroutineScope,
    private val events: Map<String, suspend (Any?) -> UiEvent>,
) {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun fire(event: String, data: Any? = null) {
        coroutineScope.launch {
            val result = events[event]!!.invoke(data)
            _uiEvent.send(result)
        }
    }
}