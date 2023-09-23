package hu.bme.aut.alkfejl.ludospringboot.data.entities

import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
@Table(name = "players")
data class PlayerEntity(
    @Id
    @GeneratedValue
    var id: Int? = null,

    @Column(name = "index")
    var index: Int,

    @Column(name = "name")
    var name: String,

    @Column(name = "standing")
    var standing: Int,

    @Column(name = "act_token")
    var actToken: Int,

    @OneToMany(mappedBy = "player", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var tokens: MutableList<TokenEntity>,

    @ManyToOne
    @JoinColumn(name = "game_id")
    var game: GameEntity,
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
        return this::class.simpleName + "(id = $id , index = $index , name = $name , standing = $standing , actToken = $actToken )"
    }

    companion object {
        fun fromName(index: Int, name: String, game: GameEntity): PlayerEntity {
            val players = PlayerEntity(
                index = index,
                name = name,
                standing = 0,
                actToken = 0,
                tokens = mutableListOf(),
                game = game,
            )
            val tokens = List(4) { TokenEntity.fromPlayer(it, players) }
            players.tokens.addAll(tokens)
            return players
        }
    }
}
