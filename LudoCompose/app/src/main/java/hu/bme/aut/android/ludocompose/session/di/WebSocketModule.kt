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

package hu.bme.aut.android.ludocompose.session.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.WebSocketClient
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WebSocketModule {
    @Provides
    @Singleton
    fun provideWebSocketClient(
        okHttpClient: OkHttpClient,
    ): WebSocketClient {
        return OkHttpWebSocketClient(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideStompClient(
        webSocketClient: WebSocketClient
    ): StompClient {
        return StompClient(webSocketClient)
    }
}
