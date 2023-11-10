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

package hu.bme.aut.android.ludocompose.session.authorization

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService


class AuthContract internal constructor(
    private val authService: AuthorizationService,
) : ActivityResultContract<AuthorizationRequest, AuthState?>() {

    override fun createIntent(context: Context, input: AuthorizationRequest): Intent {
        return authService.getAuthorizationRequestIntent(input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): AuthState? {
        if (intent == null || resultCode != RESULT_OK) return null
        val ex = AuthorizationException.fromIntent(intent)
        val resp = AuthorizationResponse.fromIntent(intent)
        return resp?.let { AuthState(it, ex) }
    }
}
