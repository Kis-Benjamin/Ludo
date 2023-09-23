package hu.bme.aut.android.ludocompose.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import hu.bme.aut.android.ludocompose.domain.model.Token

@Entity(
    tableName = "tokens",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["player_id"]),
    ],
    foreignKeys = [ForeignKey(
        entity = PlayerEntity::class,
        parentColumns = ["id"],
        childColumns = ["player_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TokenEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "player_id") var playerId: Long? = null,
    @ColumnInfo(name = "index") val index: Int,
    @ColumnInfo(name = "state") val state: Token.State = Token.State.YARD,
    @ColumnInfo(name = "track_pos") val trackPos: Int = 0
)
