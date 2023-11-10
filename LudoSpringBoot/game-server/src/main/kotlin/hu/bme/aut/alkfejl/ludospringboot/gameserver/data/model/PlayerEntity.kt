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

import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.pieceCount
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.pieceIndices
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.standingIndices
import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
@Table(name = "players")
data class PlayerEntity internal constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "index", nullable = false)
    var index: Int,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "subject", nullable = false)
    var subject: String,

    @Column(name = "standing", nullable = false)
    var standing: Int,

    @Column(name = "act_token", nullable = false)
    var actPiece: Int,

    @OneToMany(mappedBy = "player", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    var pieces: MutableList<PieceEntity>,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    var game: GameEntity? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as PlayerEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , index = $index , name = $name , standing = $standing , actPiece = $actPiece )"
    }

    @PreUpdate
    private fun preUpdate() {
        check(id != null) { "Cannot update transient entity" }
        check(index in pieceIndices) { "Index: $index, must be between ${pieceIndices.first} and ${pieceIndices.last}" }
        check(name.isNotBlank()) { "Name must not be blank" }
        check(subject.isNotBlank()) { "Subject must not be blank" }
        check(standing in standingIndices) { "Standing: $standing, must be between ${standingIndices.first} and ${standingIndices.last}" }
        check(pieces.size == pieceCount) { "Piece count: ${pieces.size} must be $pieceCount" }
        check(pieces.size == pieces.distinctBy { it.id }.size) { "Piece IDs must be unique" }
        check(actPiece in pieces.indices) { "Act piece: $actPiece, must be between 0 and ${pieces.lastIndex}" }
    }

    constructor(game: GameEntity, index: Int, name: String, subject: String) : this(
        game = game,
        index = index,
        name = name,
        subject = subject,
        standing = 0,
        actPiece = 0,
        pieces = mutableListOf(),
    ) {
        pieces = pieceIndices.map {
            PieceEntity(this, it)
        }.toMutableList()
    }
}
