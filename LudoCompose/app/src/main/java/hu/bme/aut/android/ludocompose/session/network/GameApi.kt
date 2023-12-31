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

package hu.bme.aut.android.ludocompose.session.network

import hu.bme.aut.android.ludocompose.session.model.BoardDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface GameApi {
    @GET("/api/game/{id}")
    fun getBoard(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Long,
    ): Call<BoardDTO?>?

    @POST("/api/game/{id}/select")
    fun select(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Long,
    ): Call<Unit>?

    @POST("/api/game/{id}/step")
    fun step(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Long,
    ): Call<Unit>?
}