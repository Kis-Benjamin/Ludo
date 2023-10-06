package hu.bme.aut.android.ludocompose.ui.util

import hu.bme.aut.android.ludocompose.ui.model.toUiText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class UiEventViewModel(
    private val coroutineScope: CoroutineScope,
    private val events: Map<String, suspend (Any?) -> Unit>,
) {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun fire(event: String, data: Any? = null) {
        coroutineScope.launch {
            try {
                launch(Dispatchers.IO) {
                    events[event]!!.invoke(data)
                }.join()
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    fun send(event: UiEvent) {
        coroutineScope.launch {
            _uiEvent.send(event)
        }
    }
}