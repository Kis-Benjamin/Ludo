package hu.bme.aut.android.ludocompose.ui.util

import hu.bme.aut.android.ludocompose.ui.model.toUiText
import kotlinx.coroutines.CoroutineExceptionHandler
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

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        coroutineScope.launch {
            _uiEvent.send(UiEvent.Failure(throwable.toUiText()))
        }
    }

    fun fire(event: String, data: Any? = null) {
        coroutineScope.launch(exceptionHandler) {
            val result = checkNotNull(events[event]?.invoke(data)) {
                "Event $event not found"
            }
            _uiEvent.send(result)
        }
    }
}