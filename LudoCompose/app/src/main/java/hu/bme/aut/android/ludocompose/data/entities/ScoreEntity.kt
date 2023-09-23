package hu.bme.aut.android.ludocompose.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "scores", indices = [Index(value = ["name"], unique = true)])
data class ScoreEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "win_count") val winCount: Int = 0
)