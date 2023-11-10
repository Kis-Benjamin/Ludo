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

package hu.bme.aut.android.ludocompose.session.controller

import hu.bme.aut.android.ludocompose.session.authorization.AuthManager
import hu.bme.aut.android.ludocompose.session.model.BoardDTO
import hu.bme.aut.android.ludocompose.session.network.GameApi
import hu.bme.aut.android.ludocompose.session.network.await
import hu.bme.aut.android.ludocompose.session.network.wait
import javax.inject.Inject

class GameControllerOnline @Inject constructor(
    private val gameApi: GameApi,
    private val authManager: AuthManager,
): GameController {
    private var mId: Long? = null

    override val id: Long
        get() = checkNotNull(mId) { "No game loaded" }

    override val hasActive get() = mId != null

    override suspend fun load(id: Long) {
        require(id > 0) { "Id must be positive" }
        mId = id
    }

    override suspend fun unLoad() {
        mId = null
    }

    override suspend fun getBoard(): BoardDTO {
        val accessToken = authManager.freshAccessToken()
        val call = gameApi.getBoard("Bearer $accessToken", id)!!
        return call.await()
    }

    override suspend fun select() {
        val accessToken = authManager.freshAccessToken()
        val call = gameApi.select("Bearer $accessToken", id)!!
        call.wait()
    }

    override suspend fun step() {
        val accessToken = authManager.freshAccessToken()
        val call = gameApi.step("Bearer $accessToken", id)!!
        call.wait()
    }
}
