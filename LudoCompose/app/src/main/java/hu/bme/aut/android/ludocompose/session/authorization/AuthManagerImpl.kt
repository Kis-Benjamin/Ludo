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

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.bme.aut.android.ludocompose.session.Config
import kotlinx.coroutines.suspendCancellableCoroutine
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationRequest.ResponseMode
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthManagerImpl @Inject constructor(
    @ApplicationContext context: Context,
) : AuthManager {
    private var mAuthService: AuthorizationService? = null

    private var mAuthRequest: AuthorizationRequest? = null

    private var mAuthContract: AuthContract? = null

    private var mAuthState: AuthState? = null

    private val authService: AuthorizationService
        get() = checkNotNull(mAuthService) { "AuthorizationService is not initialized" }

    private val authRequest: AuthorizationRequest
        get() = checkNotNull(mAuthRequest) { "AuthorizationRequest is not initialized" }

    override val authContract: AuthContract
        get() = checkNotNull(mAuthContract) { "AuthContract is not initialized" }

    private val authState: AuthState
        get() = checkNotNull(mAuthState) { "AuthState is not initialized" }

    override val isOnlineAvailable: Boolean
        get() = mAuthRequest != null

    override val isAuthorized: Boolean
        get() = authState.isAuthorized

    init {
        mAuthService = AuthorizationService(context)
        mAuthContract = AuthContract(mAuthService!!)
    }

    override fun launch(launcher: (AuthorizationRequest) -> Unit) {
        launcher(authRequest)
    }

    override suspend fun initialize() = suspendCancellableCoroutine { cont ->
        AuthorizationServiceConfiguration.fetchFromIssuer(
            Config.ISSUER_URI
        ) { serviceConfiguration, ex ->
            if (ex != null) {
                cont.resumeWithException(ex)
                return@fetchFromIssuer
            }
            if (serviceConfiguration == null) {
                cont.resumeWithException(IllegalStateException("ServiceConfiguration is null"))
                return@fetchFromIssuer
            }
            if (mAuthService == null) {
                cont.resumeWithException(IllegalStateException("AuthManager is not initialized in order"))
                return@fetchFromIssuer
            }
            mAuthRequest = AuthorizationRequest.Builder(
                serviceConfiguration,
                Config.CLIENT_ID,
                ResponseTypeValues.CODE,
                Config.REDIRECT_URI
            )
                .setScopes(Config.SCOPE)
                .setResponseMode(ResponseMode.QUERY)
                .build()
            mAuthState = AuthState(serviceConfiguration)
            cont.resume(Unit)
        }
    }

    override suspend fun onResult(result: AuthState?) {
        if (result == null) return
        val resp = result.lastAuthorizationResponse!!

        mAuthState = suspendCancellableCoroutine { cont ->
            authService.performTokenRequest(
                resp.createTokenExchangeRequest(
                    mapOf(
                        "client_secret" to Config.CLIENT_SECRET,
                    )
                )
            ) { tokenResponse, ex ->
                if (ex != null) {
                    cont.resumeWithException(ex)
                    return@performTokenRequest
                }
                if (tokenResponse == null) {
                    cont.resumeWithException(IllegalStateException("TokenResponse is null"))
                    return@performTokenRequest
                }
                result.update(tokenResponse, ex)
                cont.resume(result)
            }
        }
    }

    override suspend fun freshAccessToken() = suspendCancellableCoroutine { cont ->
        authState.performActionWithFreshTokens(
            authService,
            mapOf(
                "client_secret" to Config.CLIENT_SECRET,
            )
        ) { accessToken, _, ex ->
            if (ex != null) {
                cont.resumeWithException(ex)
                return@performActionWithFreshTokens
            }
            if (accessToken == null) {
                cont.resumeWithException(IllegalStateException("AccessToken is null"))
                return@performActionWithFreshTokens
            }
            cont.resume(accessToken)
        }
    }

    override fun dispose() {
        mAuthService?.dispose()
    }
}