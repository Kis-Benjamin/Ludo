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

package hu.bme.aut.alkfejl.ludospringboot.gameserver.data.model

import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.diceValues
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.playerCounts
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.playerMaxCount
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.playerMinCount
import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
@Table(name = "games")
data class GameEntity internal constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "dice", nullable = false)
    var dice: Int,

    @Column(name = "act_player", nullable = false)
    var actPlayer: Int,

    @OneToMany(mappedBy = "game", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    var players: MutableList<PlayerEntity>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as GameEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , dice = $dice , actPlayer = $actPlayer )"
    }

    @PreUpdate
    private fun preUpdate() {
        check(id != null) { "Cannot update transient entity" }
        check(dice in diceValues) { "Dice: $dice, must be between ${diceValues.first} and ${diceValues.last}" }
        check(players.size in playerCounts) { "Player count: ${players.size}, must be between $playerMinCount and $playerMaxCount" }
        check(players.size == players.distinctBy { it.id }.size) { "Player IDs must be unique" }
        check(players.size == players.distinctBy { it.name }.size) { "Player names must be unique" }
        check(players.size == players.distinctBy { it.subject }.size) { "Player user IDs must be unique" }
        check(actPlayer in players.indices) { "Act player: $actPlayer, must be between 0 and ${players.lastIndex}" }
    }

    constructor(userDetails: List<Pair<String, String>>) : this(
        dice = (1..5).random(),
        actPlayer = 0,
        players = mutableListOf(),
    ) {
        players = userDetails.mapIndexed { index, (name, userId) ->
            PlayerEntity(this, index, name, userId)
        }.toMutableList()
    }
}
