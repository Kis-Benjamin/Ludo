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

import hu.bme.aut.android.ludocompose.session.model.RoomDTO
import hu.bme.aut.android.ludocompose.session.model.RoomRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RoomApi {
    @GET("/api/room/")
    fun getAll(
        @Header("Authorization") accessToken: String,
    ): Call<List<RoomDTO>?>?

    @GET("/api/room/{id}")
    fun get(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Long,
    ): Call<RoomDTO?>?

    @POST("/api/room/")
    fun create(
        @Header("Authorization") accessToken: String,
        @Body room: RoomRequest,
    ): Call<Long?>?

    @POST("/api/room/{id}")
    fun start(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Long,
    ): Call<Unit>?

    @DELETE("/api/room/{id}")
    fun close(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Long,
    ): Call<Unit>?

    @POST("/api/room/{id}/join")
    fun join(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Long,
    ): Call<Unit>?

    @POST("/api/room/{id}/leave")
    fun leave(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Long,
    ): Call<Unit>?

    @POST("/api/room/{id}/ready")
    fun ready(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Long,
    ): Call<Unit>?

    @POST("/api/room/{id}/unready")
    fun unready(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Long,
    ): Call<Unit>?
}