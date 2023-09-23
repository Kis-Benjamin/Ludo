package hu.bme.aut.android.ludocompose.ui.model

import hu.bme.aut.android.ludocompose.domain.model.Board

data class BoardUi internal constructor(
    val yardFields: List<List<FieldUi>>,
    val trackFields: List<FieldUi>,
    val homeFields: List<List<FieldUi>>,
)

fun Board.toUiModel(): BoardUi {
    val yardPoints = BoardConstants.yardPoints
    val yardFields = List(BoardConstants.playerCount) { playerIndex ->
        List(BoardConstants.tokenCount) { tokenIndex ->
            FieldUi(
                yardPoints[playerIndex][tokenIndex],
                ColorSequence.values()[playerIndex],
            )
        }
    }
    this.yardFields.forEachIndexed { playerIndex, fields ->
        fields.forEachIndexed { tokenIndex, field ->
            val fieldUi = yardFields[playerIndex][tokenIndex]
            fieldUi.update(field)
        }
    }
    val trackPoints = BoardConstants.trackPoints
    val trackFields = List(BoardConstants.trackSize) { index ->
        FieldUi(
            trackPoints[index],
            if (index.mod(BoardConstants.trackMultiplier) == 0)
                ColorSequence.values()[index / BoardConstants.trackMultiplier]
            else ColorSequence.WHITE,
        )
    }
    this.trackFields.forEachIndexed { index, field ->
        val fieldUi = trackFields[index]
        fieldUi.update(field)
    }
    val homePoints = BoardConstants.homePoints
    val homeFields = List(BoardConstants.playerCount) { playerIndex ->
        List(BoardConstants.tokenCount) { tokenIndex ->
            FieldUi(
                homePoints[playerIndex][tokenIndex],
                ColorSequence.values()[playerIndex],
            )
        }
    }
    this.homeFields.forEachIndexed { playerIndex, fields ->
        fields.forEachIndexed { tokenIndex, field ->
            val fieldUi = homeFields[playerIndex][tokenIndex]
            fieldUi.update(field)
        }
    }
    return BoardUi(yardFields, trackFields, homeFields)
}