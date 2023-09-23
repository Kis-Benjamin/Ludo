package hu.bme.aut.android.ludocompose.ui.model

import androidx.compose.ui.geometry.Offset
import hu.bme.aut.android.ludocompose.domain.model.Board
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object BoardConstants {
    private const val SQRT2 = 1.4142135623730951
    private const val RAD = PI / 180.0
    private val Int.rad get() = (mod(360) * RAD).toFloat()
    private fun cos(deg: Int) = cos(deg.rad)
    private fun sin(deg: Int) = sin(deg.rad)

    const val playerCount = 4
    const val tokenCount = 4
    const val trackMultiplier = 10
    const val trackSize = trackMultiplier * playerCount

    // calculate points in a 1x1 clip square
    const val width = 1f
    const val height = 1f
    const val average = (height + width) / 2f
    const val halfWidth = width / 2f
    const val halfHeight = height / 2f
    const val trackPadding = average / 16f
    const val fieldRadius = average / 40f
    const val tokenRadius = average / 50f
    const val pointerRadius = average / 80f
    const val yardPadding = (trackPadding / SQRT2).toFloat()
    const val diceRadius = average / 16f
    const val diceBorder = average / 250f
    const val fieldBorder = average / 500f

    val diceOffset = Offset(halfWidth, halfHeight)

    private const val playerAngleStep = 360 / playerCount
    private const val tokenAngleStep = 360 / tokenCount
    private const val trackAngleStep = 360 / trackSize

    private const val startAngle = 225
    private const val trackStartAngle = startAngle + trackAngleStep

    val yardPoints by lazy {
        Array(playerCount) { player ->
            val outerAngle = player * playerAngleStep + startAngle
            val outerRadiusX = halfWidth + trackPadding
            val outerRadiusY = halfHeight + trackPadding
            val outerX = outerRadiusX * cos(outerAngle) + halfWidth
            val outerY = outerRadiusY * sin(outerAngle) + halfHeight
            Array(tokenCount) { token ->
                val innerAngle = token * tokenAngleStep + startAngle
                val innerX = yardPadding * cos(innerAngle)
                val innerY = yardPadding * sin(innerAngle)
                Offset(
                    outerX - innerX,
                    outerY - innerY
                )
            }
        }
    }

    val trackPoints by lazy {
        val radiusX = halfWidth - trackPadding
        val radiusY = halfHeight - trackPadding
        Array(trackSize) { track ->
            val angle = track * trackAngleStep + trackStartAngle
            Offset(
                (radiusX * cos(angle) + halfWidth),
                (radiusY * sin(angle) + halfHeight)
            )
        }
    }

    val homePoints by lazy {
        Array(playerCount) { player ->
            val angle = player * playerAngleStep + startAngle
            Array(tokenCount) { token ->
                val radiusDiff = (token + 2) * trackPadding
                Offset(
                    (halfWidth - radiusDiff) * cos(angle) + halfWidth,
                    (halfHeight - radiusDiff) * sin(angle) + halfHeight
                )
            }
        }
    }
}
