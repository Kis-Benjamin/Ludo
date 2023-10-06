package hu.bme.aut.android.ludocompose.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoadingViewModel(
    private val coroutineScope: CoroutineScope,
    private val loadData: suspend () -> Result<Unit>,
) {

    private val _state = MutableStateFlow(LoadingState())
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load(requestReset: Boolean = false) {
        coroutineScope.launch {
            try {
                if (requestReset) {
                    _state.update {
                        it.copy(isLoading = true)
                    }
                }
                loadData().getOrThrow()
                _state.update {
                    it.copy(isLoading = false)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, error = e)
                }
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
