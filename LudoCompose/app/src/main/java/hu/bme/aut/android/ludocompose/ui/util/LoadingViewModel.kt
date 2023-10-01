package hu.bme.aut.android.ludocompose.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class LoadingViewModel<T : LoadingState> : ViewModel() {

    protected abstract val _state: MutableStateFlow<T>
    abstract val state: StateFlow<T>

    protected fun init() {
        viewModelScope.launch {
            _state.update {
                // TODO: 2023 .10. 01. Avoid casting
                it.clone(isLoading = true) as T
            }
            load()
        }
    }

    protected abstract suspend fun loadImpl()

    protected fun load() {
        viewModelScope.launch {
            CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                try {
                    loadImpl()
                } catch (e: Exception) {
                    _state.update {
                        // TODO: 2023. 10. 01. Avoid casting
                        it.clone(isLoading = false, error = e) as T
                    }
                }
            }
        }
    }
}

interface LoadingState {
    val isLoading: Boolean
    val isError: Boolean
    val error: Throwable?
    // TODO: 2023. 10. 01. LoadingState should be a data class
    fun clone(
        isLoading: Boolean = this.isLoading,
        error: Throwable? = this.error,
    ): LoadingState
}
