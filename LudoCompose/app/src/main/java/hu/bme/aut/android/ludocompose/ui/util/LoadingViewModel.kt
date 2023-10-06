package hu.bme.aut.android.ludocompose.ui.util

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class LoadingViewModel(
    private val coroutineScope: CoroutineScope,
    private val loadData: suspend () -> Unit,
) {

    private val _state = MutableStateFlow(LoadingState())
    val state = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        coroutineScope.launch {
            _state.update { state ->
                state.copy(isLoading = false, error = throwable)
            }
        }
    }

    init {
        load()
    }

    fun load(requestReset: Boolean = false) {
        coroutineScope.launch(exceptionHandler) {
            if (requestReset) {
                _state.update { state ->
                    state.copy(isLoading = true)
                }
            }
            loadData() // Possible to throw exception
            _state.update { state ->
                state.copy(isLoading = false)
            }
        }
    }
}

data class LoadingState(
    val isLoading: Boolean = true,
    val error: Throwable? = null,
) {
    val isError: Boolean get() = error != null
}
