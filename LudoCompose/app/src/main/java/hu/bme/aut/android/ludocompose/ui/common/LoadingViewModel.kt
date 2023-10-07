/*
 * Copyright © 2023 Benjamin Kis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.bme.aut.android.ludocompose.ui.common

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun load() {
        coroutineScope.launch(exceptionHandler) {
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
