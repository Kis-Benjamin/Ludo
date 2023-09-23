package hu.bme.aut.android.ludocompose.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import hu.bme.aut.android.ludocompose.data.converters.LocalDateConverter
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "games",
    indices = [Index(value = ["id"], unique = true)]
)
@TypeConverters(LocalDateConverter::class)
data class GameEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "dice") val dice: Int,
    @ColumnInfo(name = "act_player") val actPlayer: Int,
)