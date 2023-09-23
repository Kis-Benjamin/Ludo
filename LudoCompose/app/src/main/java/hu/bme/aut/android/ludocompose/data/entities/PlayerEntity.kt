package hu.bme.aut.android.ludocompose.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "players",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["game_id"])
    ],
    foreignKeys = [ForeignKey(
        entity = GameEntity::class,
        parentColumns = ["id"],
        childColumns = ["game_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PlayerEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "game_id") var gameId: Long? = null,
    @ColumnInfo(name = "index") val index: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "standing") val standing: Int,
    @ColumnInfo(name = "act_token") val actToken: Int = 0,
)
