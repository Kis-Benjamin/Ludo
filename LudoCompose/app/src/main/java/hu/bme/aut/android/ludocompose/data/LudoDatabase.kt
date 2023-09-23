package hu.bme.aut.android.ludocompose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.bme.aut.android.ludocompose.data.converters.LocalDateConverter
import hu.bme.aut.android.ludocompose.data.converters.TokenStateConverter
import hu.bme.aut.android.ludocompose.data.dao.GameDao
import hu.bme.aut.android.ludocompose.data.dao.ScoreDao
import hu.bme.aut.android.ludocompose.data.entities.GameEntity
import hu.bme.aut.android.ludocompose.data.entities.PlayerEntity
import hu.bme.aut.android.ludocompose.data.entities.ScoreEntity
import hu.bme.aut.android.ludocompose.data.entities.TokenEntity

@Database(
    entities = [
        GameEntity::class,
        PlayerEntity::class,
        TokenEntity::class,
        ScoreEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class, TokenStateConverter::class)
abstract class LudoDatabase : RoomDatabase() {
    abstract val gameDao: GameDao
    abstract val scoreDao: ScoreDao
}