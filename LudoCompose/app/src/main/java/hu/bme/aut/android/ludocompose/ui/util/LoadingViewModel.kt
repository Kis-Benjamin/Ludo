package hu.bme.aut.android.ludocompose.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoadingViewModel(
    val coroutineScope: CoroutineScope,
    val loadImpl: suspend () -> Unit,
) {

    private val _state = MutableStateFlow(LoadingState())
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                loadImpl()
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
