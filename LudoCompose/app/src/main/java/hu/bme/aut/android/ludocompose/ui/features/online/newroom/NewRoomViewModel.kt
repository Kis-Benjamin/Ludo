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

package hu.bme.aut.android.ludocompose.ui.features.online.newroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.session.controller.RoomController
import hu.bme.aut.android.ludocompose.session.di.Online
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEvent
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEventViewModel
import hu.bme.aut.android.ludocompose.ui.model.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NewRoomViewModel @Inject constructor(
    @Online
    private val roomController: RoomController,
) : ViewModel() {

    private val _state = MutableStateFlow(NewRoomState())
    val state = _state.asStateFlow()

    private val uiEventViewModel = UiEventViewModel(viewModelScope)

    val uiEvent = uiEventViewModel.uiEvent



    fun setName(name: String) {
        _state.update { state ->
            state.copy(name = name)
        }
    }

    fun create() = uiEventViewModel.handleWith {
        val name = _state.value.name
        if (name.isBlank()) {
            UiEvent.Failure(UiText.StringResource(R.string.new_room_name_empty))
        } else {
            roomController.create(name)
            UiEvent.Success
        }

    }
}

data class NewRoomState(
    val name: String = "",
)