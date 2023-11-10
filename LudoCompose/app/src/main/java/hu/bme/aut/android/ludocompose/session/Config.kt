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

package hu.bme.aut.android.ludocompose.session

import android.net.Uri

data object Config {
    private const val ISSUER = "https://192.168.1.105:8443/realms/ludo"
    val ISSUER_URI = Uri.parse(ISSUER)!!

    const val CLIENT_ID = "ludo-client"

    const val CLIENT_SECRET = "Queo2GUaGZ5pDORr1qP2s7yeeY99c7qc"

    private const val REDIRECT = "ludo://ludo-compose/callback"
    val REDIRECT_URI = Uri.parse(REDIRECT)!!

    const val SCOPE = "openid profile ludo.user ludo.host"

    const val HTTP_URL = "http://192.168.1.105:8080/"

    const val WEBSOCKET_URL = "ws://192.168.1.105:8080/ws"
}