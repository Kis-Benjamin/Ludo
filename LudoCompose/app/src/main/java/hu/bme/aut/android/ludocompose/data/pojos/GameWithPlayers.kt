package hu.bme.aut.android.ludocompose.data.pojos

import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.TypeConverters
import hu.bme.aut.android.ludocompose.data.converters.LocalDateConverter
import hu.bme.aut.android.ludocompose.data.entities.GameEntity
import hu.bme.aut.android.ludocompose.data.entities.PlayerEntity

@TypeConverters(LocalDateConverter::class)
data class GameWithPlayers(
    @Embedded val game: GameEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "game_id",
        entity = PlayerEntity::class,
    ) val players: List<PlayerWithTokens>
)
