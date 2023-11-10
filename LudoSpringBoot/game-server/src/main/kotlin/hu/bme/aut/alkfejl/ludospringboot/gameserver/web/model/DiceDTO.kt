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

package hu.bme.aut.alkfejl.ludospringboot.gameserver.web.model

import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.model.Dice

data class DiceDTO internal constructor(
    val value: Int = 0,
    val color: Int? = null,
)

fun Dice.toSessionModel() = DiceDTO(
    value = value,
    color = color,
)
