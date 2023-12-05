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

package hu.bme.aut.android.ludocompose.ui.features.online.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.session.controller.GameController
import hu.bme.aut.android.ludocompose.session.controller.RoomController
import hu.bme.aut.android.ludocompose.session.di.Online
import hu.bme.aut.android.ludocompose.session.model.RoomClose
import hu.bme.aut.android.ludocompose.session.model.RoomStart
import hu.bme.aut.android.ludocompose.session.model.RoomUpdate
import hu.bme.aut.android.ludocompose.session.stomp.StompManager
import hu.bme.aut.android.ludocompose.ui.features.common.load.LoadingViewModel
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEvent
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEventViewModel
import hu.bme.aut.android.ludocompose.ui.model.RoomUi
import hu.bme.aut.android.ludocompose.ui.model.UiText
import hu.bme.aut.android.ludocompose.ui.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.conversions.subscribe
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    @Online
    private val roomController: RoomController,
    @Online
    private val gameController: GameController,
    private val stompManager: StompManager,
) : ViewModel() {

    private val _state = MutableStateFlow(RoomState())
    val state = _state.asStateFlow()

    val loadingViewModel = LoadingViewModel(viewModelScope) {
        val room = roomController.get()
        _state.update { state ->
            state.copy(
                room = room.toUiModel(),
                isReadyToStart = room.users.all { it.ready }
            )
        }
    }

    private val uiEventViewModel = UiEventViewModel(viewModelScope)

    val uiEvent get() = uiEventViewModel.uiEvent

    init {
        uiEventViewModel.handleWith {
            stompManager.session
                .subscribe<RoomStart>("/room/start")
                .first { it.id == roomController.id }.let {
                    roomController.unLoad()
                    gameController.load(it.gameId)
                }
            UiEvent.Success
        }
        uiEventViewModel.handleWith {
            stompManager.session
                .subscribe<RoomUpdate>("/room/update")
                .collect {
                    if (it.id == roomController.id) {
                        loadingViewModel.load()
                    }
                }
            UiEvent.Failure(UiText.StringResource(R.string.connection_error))
        }
        uiEventViewModel.handleWith {
            stompManager.session
                .subscribe<RoomClose>("/room/close")
                .first { it.id == roomController.id }.let {
                    roomController.unLoad()
                }
            UiEvent.Close
        }
    }

    fun start() = uiEventViewModel.handleWith {
        roomController.start()
        UiEvent.Continue
    }

    fun changeReadyState() = uiEventViewModel.handleWith {
        if (state.value.ready) {
            roomController.unready()
        } else {
            roomController.ready()
        }
        _state.update { state ->
            state.copy(ready = !state.ready)
        }
        UiEvent.Continue
    }

    fun leave() = uiEventViewModel.handleWith {
        roomController.leave()
        UiEvent.Close
    }

    fun close() = uiEventViewModel.handleWith {
        roomController.close()
        UiEvent.Continue
    }
}

data class RoomState(
    val room: RoomUi = RoomUi(),
    val ready: Boolean = false,
    val isReadyToStart: Boolean = false,
)
