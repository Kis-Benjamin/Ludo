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

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.ludocompose.session.controller.GameController
import hu.bme.aut.android.ludocompose.session.controller.RoomController
import hu.bme.aut.android.ludocompose.session.controller.ScoreController
import hu.bme.aut.android.ludocompose.session.controller.ScoreControllerLocal
import hu.bme.aut.android.ludocompose.session.manager.GameManager
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ControllerModule {
    @Local
    @Binds
    @Singleton
    abstract fun bindGameControllerLocal(
        gameControllerLocal: GameManager
    ): GameController

    @Local
    @Binds
    @Singleton
    abstract fun bindScoreControllerLocal(
        scoreControllerLocal: ScoreControllerLocal
    ): ScoreController
}
