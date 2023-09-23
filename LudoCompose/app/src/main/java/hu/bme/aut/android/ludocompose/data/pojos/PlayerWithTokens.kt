package hu.bme.aut.android.ludocompose.data.pojos

import androidx.room.Embedded
import androidx.room.Relation
import hu.bme.aut.android.ludocompose.data.entities.PlayerEntity
import hu.bme.aut.android.ludocompose.data.entities.TokenEntity

data class PlayerWithTokens(
    @Embedded val player: PlayerEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "player_id",
        entity = TokenEntity::class,
    ) val tokens: List<TokenEntity>
)
