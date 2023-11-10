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

import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.pieceIndices
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.trackPositions
import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
@Table(name = "pieces")
data class PieceEntity internal constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "index", nullable = false)
    var index: Int,

    @Column(name = "track_pos", nullable = false)
    var trackPos: Int,

    @Column(name = "state", nullable = false)
    var state: Int,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    var player: PlayerEntity? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as PieceEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , index = $index , trackPos = $trackPos , state = $state )"
    }

    @PreUpdate
    private fun preUpdate() {
        check(id != null) { "Cannot update transient entity" }
        check(index in pieceIndices) { "Index: $index, must be between ${pieceIndices.first} and ${pieceIndices.last}" }
        check(trackPos in trackPositions) { "Track position: $trackPos, must be between ${trackPositions.first} and ${trackPositions.last}" }
        check(state >= 0) { "State: $state, must be non-negative" }
    }

    constructor(player: PlayerEntity, index: Int) : this(
        player = player,
        index = index,
        state = 0,
        trackPos = 0,
    )
}
