package com.slyworks.cryptocompose.ui.screens.details_activity

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection


/**
 *Created by Joshua Sylvanus, 1:56 PM, 25/09/2022.
 */
class CurveShape : Shape {
    private fun getCurveFor(size: Size): Path {
        val width:Float = size.width
        val height:Float = size.height
        val radius:Float = 100f
        val upShift:Float = height * (1f - 0.5f)

        return Path().apply {
            reset()

            /* arc 1 */
            arcTo(
                rect = Rect(left = 0f, top = 0f, right = (radius * 2), bottom = (radius * 2)),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 110f,
                forceMoveTo = false )

            /* arc 2 */
            arcTo(
                rect = Rect(left = (width - radius * 2), top = (upShift - 10), right = width, bottom = (upShift + radius * 2) ),
                startAngleDegrees = -60f,
                sweepAngleDegrees = 65f,
                forceMoveTo = false )

            /* arc 3 */
            arcTo(
                rect = Rect(left = (width - radius * 2), top = (height - radius * 2), right = width, bottom = height),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false )

            /* arc 4 */
            arcTo(
                rect = Rect(left = 0f, top = (height - radius * 2), right = (radius * 2), bottom = height),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false )
        }
    }

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        return Outline.Generic(path = getCurveFor(size))
    }
}
