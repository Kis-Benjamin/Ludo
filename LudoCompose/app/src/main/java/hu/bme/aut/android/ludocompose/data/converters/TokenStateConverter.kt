package hu.bme.aut.android.ludocompose.data.converters

import androidx.room.TypeConverter
import hu.bme.aut.android.ludocompose.domain.model.Token

object TokenStateConverter {
    @TypeConverter
    fun Token.State.asString(): String = name

    @TypeConverter
    fun String.asTokenState(): Token.State = try {
        Token.State.valueOf(this)
    } catch (e: IllegalArgumentException) {
        Token.State.YARD
    }
}