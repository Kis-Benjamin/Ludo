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

import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> Call<T?>.await(): T {
    return suspendCancellableCoroutine { cont ->
        enqueue(object : Callback<T?> {
            override fun onResponse(
                call: Call<T?>,
                response: Response<T?>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        cont.resume(body)
                        return
                    }
                }
                cont.resumeWithException(Exception(response.message() ?: "Error getting messages"))
            }

            override fun onFailure(call: Call<T?>, t: Throwable) {
                cont.resumeWithException(t)
            }
        })
    }
}

suspend fun Call<Unit>.wait() {
    suspendCancellableCoroutine { cont ->
        enqueue(object : Callback<Unit> {
            override fun onResponse(
                call: Call<Unit>,
                response: Response<Unit>
            ) {
                if (response.isSuccessful) {
                    cont.resume(Unit)
                } else {
                    cont.resumeWithException(Exception(response.message() ?: "Error getting messages"))
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                cont.resumeWithException(t)
            }
        })
    }
}
