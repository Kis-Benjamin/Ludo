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

package hu.bme.aut.android.ludocompose.session.stomp

import com.squareup.moshi.Moshi
import hu.bme.aut.android.ludocompose.session.Config
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.conversions.TypedStompSession
import org.hildan.krossbow.stomp.conversions.moshi.withMoshi
import javax.inject.Inject

class StompManagerImpl @Inject constructor(
    private val moshi: Moshi,
    private val stompClient: StompClient,
) : StompManager {
    private var mSession: TypedStompSession? = null

    override val session: TypedStompSession
        get() = checkNotNull(mSession) { "StompManager not initialized" }

    override suspend fun initialize() {
        if (mSession != null) {
            dispose()
        }
        mSession = stompClient.connect(Config.WEBSOCKET_URL).withMoshi(moshi)
    }

    override suspend fun dispose() {
        mSession?.disconnect()
        mSession = null
    }
}
