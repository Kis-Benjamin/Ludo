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
import hu.bme.aut.android.ludocompose.session.model.RoomDTO
import hu.bme.aut.android.ludocompose.session.model.RoomRequest
import hu.bme.aut.android.ludocompose.session.network.RoomApi
import hu.bme.aut.android.ludocompose.session.network.await
import hu.bme.aut.android.ludocompose.session.network.wait
import javax.inject.Inject

class RoomControllerOnline @Inject constructor(
    private val roomApi: RoomApi,
    private val authManager: AuthManager,
) : RoomController {
    private var mId: Long? = null

    override val id: Long
        get() = checkNotNull(mId) { "No room loaded" }

    override val hasActive get() = mId != null

    override suspend fun load(id: Long) {
        require(id > 0) { "Id must be positive" }
        if (hasActive) {
            unLoad()
        }
        mId = id
    }

    override suspend fun unLoad() {
        mId = null
    }

    override suspend fun getAll(): List<RoomDTO> {
        val accessToken = authManager.freshAccessToken()
        val call = roomApi.getAll("Bearer $accessToken")!!
        return call.await()
    }

    override suspend fun get(id: Long): RoomDTO {
        val accessToken = authManager.freshAccessToken()
        val call = roomApi.get("Bearer $accessToken", id)!!
        return call.await()
    }

    override suspend fun get(): RoomDTO {
        val accessToken = authManager.freshAccessToken()
        val call = roomApi.get("Bearer $accessToken", id)!!
        return call.await()
    }

    override suspend fun create(name: String) {
        val accessToken = authManager.freshAccessToken()
        val roomRequest = RoomRequest(name)
        val call = roomApi.create("Bearer $accessToken", roomRequest)!!
        val id = call.await()
        load(id)
    }

    override suspend fun start() {
        val accessToken = authManager.freshAccessToken()
        val call = roomApi.start("Bearer $accessToken", id)!!
        call.wait()
    }

    override suspend fun close() {
        val accessToken = authManager.freshAccessToken()
        val call = roomApi.close("Bearer $accessToken", id)!!
        call.wait()
    }

    override suspend fun join(id: Long) {
        require(id > 0) { "Id must be positive" }
        val accessToken = authManager.freshAccessToken()
        val call = roomApi.join("Bearer $accessToken", id)!!
        call.wait()
        load(id)
    }

    override suspend fun leave() {
        val accessToken = authManager.freshAccessToken()
        val call = roomApi.leave("Bearer $accessToken", id)!!
        call.wait()
        unLoad()
    }

    override suspend fun ready() {
        val accessToken = authManager.freshAccessToken()
        val call = roomApi.ready("Bearer $accessToken", id)!!
        call.wait()
    }

    override suspend fun unready() {
        val accessToken = authManager.freshAccessToken()
        val call = roomApi.unready("Bearer $accessToken", id)!!
        call.wait()
    }

}
