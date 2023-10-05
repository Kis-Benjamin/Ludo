package hu.bme.aut.android.ludocompose.ui.model

import hu.bme.aut.android.ludocompose.domain.model.Board
import hu.bme.aut.android.ludocompose.domain.model.Constants.playerCount
import hu.bme.aut.android.ludocompose.domain.model.Constants.tokenCount
import hu.bme.aut.android.ludocompose.domain.model.Constants.trackMultiplier
import hu.bme.aut.android.ludocompose.domain.model.Constants.trackSize

data object BoardUi {
    val yardFields: List<List<FieldUi>> by lazy {
        List(playerCount) { playerIndex ->
            List(tokenCount) { tokenIndex ->
                FieldUi(
                    Constants.yardPoints[playerIndex][tokenIndex],
                    ColorSequence.entries[playerIndex],
                )
            }
        }
    }

    val trackFields: List<FieldUi> by lazy {
        List(trackSize) { index ->
            FieldUi(
                Constants.trackPoints[index],
                if (index.mod(trackMultiplier) == 0)
                    ColorSequence.entries[index / trackMultiplier]
                else ColorSequence.WHITE,
            )
        }
    }

    val homeFields: List<List<FieldUi>> by lazy {
        List(playerCount) { playerIndex ->
            List(tokenCount) { tokenIndex ->
                FieldUi(
                    Constants.homePoints[playerIndex][tokenIndex],
                    ColorSequence.entries[playerIndex],
                )
            }
        }
    }

    fun update(board: Board) {
        board.yardFields.forEachIndexed { playerIndex, fields ->
            fields.forEachIndexed { tokenIndex, field ->
                yardFields[playerIndex][tokenIndex].update(field)
            }
        }
        board.trackFields.forEachIndexed { index, field ->
            trackFields[index].update(field)
        }
        board.homeFields.forEachIndexed { playerIndex, fields ->
            fields.forEachIndexed { tokenIndex, field ->
                homeFields[playerIndex][tokenIndex].update(field)
            }
        }
    }
}
