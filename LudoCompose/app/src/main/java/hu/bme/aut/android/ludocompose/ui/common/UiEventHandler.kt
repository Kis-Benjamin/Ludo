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

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import hu.bme.aut.android.ludocompose.ui.model.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun UiEventHandler(
    uiEvent: Flow<UiEvent>,
    snackbarHostState: SnackbarHostState,
    onSuccess: (UiEvent.Success) -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        uiEvent.collect { uiEvent ->
            fun send(message: UiText) {
                scope.launch {
                    snackbarHostState.showSnackbar(message.asString(context))
                }
            }
            when (uiEvent) {
                is UiEvent.Success -> {
                    onSuccess(uiEvent)
                }
                is UiEvent.Settled -> {
                    send(uiEvent.message)
                }
                is UiEvent.Failure -> {
                    send(uiEvent.message)
                }
            }
        }
    }
}
