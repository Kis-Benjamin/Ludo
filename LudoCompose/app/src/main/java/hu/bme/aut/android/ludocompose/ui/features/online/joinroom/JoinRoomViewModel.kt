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

package hu.bme.aut.android.ludocompose.ui.features.online.joinroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.session.controller.RoomController
import hu.bme.aut.android.ludocompose.session.di.Online
import hu.bme.aut.android.ludocompose.session.model.RoomClose
import hu.bme.aut.android.ludocompose.session.model.RoomCreate
import hu.bme.aut.android.ludocompose.session.model.RoomStart
import hu.bme.aut.android.ludocompose.session.stomp.StompManager
import hu.bme.aut.android.ludocompose.ui.features.common.load.LoadingViewModel
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEvent
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEventViewModel
import hu.bme.aut.android.ludocompose.ui.model.RoomUi
import hu.bme.aut.android.ludocompose.ui.model.UiText
import hu.bme.aut.android.ludocompose.ui.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.conversions.subscribe
import javax.inject.Inject

@HiltViewModel
class JoinRoomViewModel @Inject constructor(
    @Online
    private val roomController: RoomController,
    private val stompManager: StompManager,
) : ViewModel() {

    private val _state = MutableStateFlow(JoinRoomState())
    val state = _state.asStateFlow()

    val loadingViewModel = LoadingViewModel(viewModelScope) {
        val rooms = roomController.getAll()
        val roomUis = rooms.map { it.toUiModel() }
        _state.update { state ->
            state.copy(rooms = roomUis)
        }
    }

    private val uiEventViewModel = UiEventViewModel(viewModelScope)

    val uiEvent get() = uiEventViewModel.uiEvent

    init {
        uiEventViewModel.handleWith {
            stompManager.session
                .subscribe<RoomCreate>("/room/create")
                .collect {
                    val room = roomController.get(it.id)
                    _state.update { state ->
                        state.copy(
                            rooms = state.rooms + room.toUiModel()
                        )
                    }
                }
            UiEvent.Failure(UiText.DynamicString("Stomp session closed"))
        }
        uiEventViewModel.handleWith {
            stompManager.session
                .subscribe<RoomClose>("/room/close")
                .collect {
                    _state.update { state ->
                        state.copy(
                            rooms = state.rooms.filter { room -> room.id != it.id }
                        )
                    }
                }
            UiEvent.Failure(UiText.DynamicString("Stomp session closed"))
        }
        uiEventViewModel.handleWith {
            stompManager.session
                .subscribe<RoomStart>("/room/start")
                .collect {
                    _state.update { state ->
                        state.copy(
                            rooms = state.rooms.filter { room -> room.id != it.id }
                        )
                    }
                }
            UiEvent.Failure(UiText.DynamicString("Stomp session closed"))
        }
    }

    fun select(index: Int) {
        _state.update { state ->
            state.copy(selectedIndex = index)
        }
    }

    fun join() = uiEventViewModel.handleWith {
        val selectedId = _state.value.selectedId
        if (selectedId == null) {
            val message = UiText.StringResource(R.string.join_room_empty_selection)
            UiEvent.Failure(message)
        } else {
            roomController.join(selectedId)
            UiEvent.Success
        }

    }
}

data class JoinRoomState(
    val rooms: List<RoomUi> = emptyList(),
    val selectedIndex: Int? = null,
) {
    val selectedId: Long?
        get() = selectedIndex?.let { rooms.getOrNull(it)?.id }
}