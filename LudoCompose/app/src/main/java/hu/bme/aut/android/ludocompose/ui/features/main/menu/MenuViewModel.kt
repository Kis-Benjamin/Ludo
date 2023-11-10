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

package hu.bme.aut.android.ludocompose.ui.features.main.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.session.authorization.AuthManager
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEvent
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEventViewModel
import hu.bme.aut.android.ludocompose.ui.model.UiText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import javax.inject.Inject


@HiltViewModel
class MenuViewModel @Inject constructor(
    private val authManager: AuthManager,
) : ViewModel() {

    private val _state = MutableStateFlow(MenuState())
    val state = _state.asStateFlow()

    private val uiEventViewModel = UiEventViewModel(
        coroutineScope = viewModelScope,
    )

    val uiEvent get() = uiEventViewModel.uiEvent

    val authContract get() = authManager.authContract

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _state.update { it.copy(isOnlineEnabled = false) }
    }

    fun launch(launcher: (AuthorizationRequest) -> Unit) = authManager.launch(launcher)

    fun initialize() {
        viewModelScope.launch(exceptionHandler) {
            authManager.initialize() // Expected to throw an exception if not online
            _state.update { it.copy(isOnlineEnabled = authManager.isOnlineAvailable) }
        }
    }

    fun onResult(result: AuthState?) = uiEventViewModel.handleWith {
        authManager.onResult(result)
        if (authManager.isAuthorized) {
            UiEvent.Success
        } else {
            val message = UiText.StringResource(R.string.login_failed)
            UiEvent.Failure(message)
        }
    }

    override fun onCleared() {
        authManager.dispose()
        super.onCleared()
    }
}

data class MenuState(
    val isOnlineEnabled: Boolean = false,
)
