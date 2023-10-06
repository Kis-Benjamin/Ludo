package hu.bme.aut.android.ludocompose.domain.converters

import hu.bme.aut.android.ludocompose.data.entities.ScoreEntity
import hu.bme.aut.android.ludocompose.domain.model.ScoreItem

fun ScoreEntity.toDomainModel() = ScoreItem(
    id = id!!,
    name = name,
    winCount = winCount,
)

fun ScoreItem.toDataModel() = ScoreEntity(
    id = null,
    name = name,
    winCount = winCount,
)

