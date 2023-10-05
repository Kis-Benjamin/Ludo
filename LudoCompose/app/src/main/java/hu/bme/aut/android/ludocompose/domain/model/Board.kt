package hu.bme.aut.android.ludocompose.domain.model

import hu.bme.aut.android.ludocompose.domain.model.Constants.playerCount
import hu.bme.aut.android.ludocompose.domain.model.Constants.tokenCount
import hu.bme.aut.android.ludocompose.domain.model.Constants.trackSize

data object Board {
    val yardFields: List<List<Field>> by lazy {
        List(playerCount) { playerIndex ->
            List(tokenCount) { tokenIndex ->
                Field()
            }
        }
    }

    val trackFields: List<Field> by lazy {
        List(trackSize) { index ->
            Field()
        }
    }

    val homeFields: List<List<Field>> by lazy {
        List(playerCount) { playerIndex ->
            List(tokenCount) { tokenIndex ->
                Field()
            }
        }
    }

    fun reset() {
        yardFields.forEach { fields ->
            fields.forEach { field ->
                field.reset()
            }
        }
        trackFields.forEach { field ->
            field.reset()
        }
        homeFields.forEach { fields ->
            fields.forEach { field ->
                field.reset()
            }
        }
    }
}
