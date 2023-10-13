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

package hu.bme.aut.android.ludocompose.ui.features.common.uievent

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
            val event = checkNotNull(events[event]) { "Event $event not found" }
            val result = event(data)
            _uiEvent.send(result)
        }
    }
}