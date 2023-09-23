package hu.bme.aut.alkfejl.ludospringboot.data.entities

import java.time.LocalDateTime
import jakarta.persistence.*
import org.hibernate.Hibernate


@Entity
@Table(name = "games")
data class GameEntity(
    @Id
    @GeneratedValue
    var id: Int? = null,

    @Column(name = "date")
    var creationDate: LocalDateTime? = null,

    @Column(name = "dice")
    var dice: Int,

    @Column(name = "act_player")
    var actPlayer: Int,

    @OneToMany(mappedBy = "game", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var players: MutableList<PlayerEntity>,
) {
    @PrePersist
    private fun prePersist() {
        creationDate = LocalDateTime.now()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as GameEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id  , creationDate = $creationDate , dice = $dice , actPlayer = $actPlayer )"
    }

    companion object {
        fun fromNames(names: List<String>): GameEntity {
            val playerCount = names.size
            val game = GameEntity(
                dice = 0,
                actPlayer = 0,
                players = mutableListOf(),
            )
            val players = List(playerCount) { PlayerEntity.fromName(it, names[it], game) }
            game.players.addAll(players)
            return game
        }
    }
}
