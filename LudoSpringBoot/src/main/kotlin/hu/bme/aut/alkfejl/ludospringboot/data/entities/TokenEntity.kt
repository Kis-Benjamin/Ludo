package hu.bme.aut.alkfejl.ludospringboot.data.entities

import hu.bme.aut.alkfejl.ludospringboot.domain.model.TokenState
import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
@Table(name = "tokens")
data class TokenEntity(
    @Id
    @GeneratedValue
    var id: Int? = null,

    @Column(name = "index")
    var index: Int,

    @Column(name = "track_pos")
    var trackPos: Int = 0,

    @Column(name = "state")
    var state: Int = TokenState.YARD,

    @ManyToOne
    @JoinColumn(name = "player_id")
    var player: PlayerEntity,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as TokenEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , index = $index , trackPos = $trackPos , state = $state )"
    }

    companion object {
        fun fromPlayer(index: Int, player: PlayerEntity): TokenEntity {
            return TokenEntity(
                index = index,
                player = player,
            )
        }
    }
}
