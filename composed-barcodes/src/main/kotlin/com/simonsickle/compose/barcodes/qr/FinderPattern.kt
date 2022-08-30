package com.simonsickle.compose.barcodes.qr

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.simonsickle.compose.barcodes.QrCodeAppearance
import com.simonsickle.compose.barcodes.path

internal const val FINDER_PATTERN_ROW_COUNT = 7
internal const val DEFAULT_ROUNDED_ARC_RATIO = 0.29f
internal const val FINDER_PATTERN_AREA = FINDER_PATTERN_ROW_COUNT * FINDER_PATTERN_ROW_COUNT
private const val INTERIOR_EXTERIOR_SHAPE_RATIO = 3f / FINDER_PATTERN_ROW_COUNT
private const val INTERIOR_EXTERIOR_OFFSET_RATIO = 2f / FINDER_PATTERN_ROW_COUNT
private const val INTERIOR_BACKGROUND_EXTERIOR_SHAPE_RATIO = 5f / FINDER_PATTERN_ROW_COUNT
private const val INTERIOR_BACKGROUND_EXTERIOR_OFFSET_RATIO = 1f / FINDER_PATTERN_ROW_COUNT

/**
 *  A valid QR code has three finder patterns (top left, top right, bottom left).
 */
internal fun DrawScope.drawFinders(
    qrCodeAppearance: QrCodeAppearance,
    marginSize: Float,
    sideLength: Float,
    finderPatternSize: Size,
    foregroundPaint: Paint
) {
    val cornerRadius = when (qrCodeAppearance) {
        QrCodeAppearance.Standard -> CornerRadius(0f, 0f)
        QrCodeAppearance.Rounded,
        QrCodeAppearance.Connected -> (finderPatternSize.width * DEFAULT_ROUNDED_ARC_RATIO).let {
            CornerRadius(it, it)
        }
    }

    setOf(
        // Draw top left finder pattern.
        Offset(x = marginSize, y = marginSize),
        // Draw top right finder pattern.
        Offset(x = sideLength - (marginSize + finderPatternSize.width), y = marginSize),
        // Draw bottom finder pattern.
        Offset(x = marginSize, y = sideLength - (marginSize + finderPatternSize.height))
    ).forEach { offset ->
        drawFinder(
            topLeft = offset,
            finderPatternSize = finderPatternSize,
            cornerRadius = cornerRadius,
            foregroundPaint = foregroundPaint
        )
    }
}

/**
 * This func is responsible for drawing a single finder pattern, for a QR code
 */
private fun DrawScope.drawFinder(
    topLeft: Offset,
    finderPatternSize: Size,
    cornerRadius: CornerRadius,
    foregroundPaint: Paint,
) = drawIntoCanvas {
    it.drawPath(
        path {
            // Draw the outer rectangle for the finder pattern.
            addRoundRect(
                roundRect = RoundRect(
                    rect = Rect(
                        offset = topLeft,
                        size = finderPatternSize
                    ),
                    cornerRadius = cornerRadius
                )
            )

            // Draw background for the finder pattern interior (this keeps the arc ratio consistent).
            val innerBackgroundOffset = Offset(
                x = finderPatternSize.width * INTERIOR_BACKGROUND_EXTERIOR_OFFSET_RATIO,
                y = finderPatternSize.height * INTERIOR_BACKGROUND_EXTERIOR_OFFSET_RATIO
            )
            addRoundRect(
                roundRect = RoundRect(
                    rect = Rect(
                        offset = topLeft + innerBackgroundOffset,
                        size = finderPatternSize * INTERIOR_BACKGROUND_EXTERIOR_SHAPE_RATIO
                    ),
                    cornerRadius = cornerRadius * 0.5f
                )
            )

            // Draw the inner rectangle for the finder pattern.
            val innerRectOffset = Offset(
                x = finderPatternSize.width * INTERIOR_EXTERIOR_OFFSET_RATIO,
                y = finderPatternSize.height * INTERIOR_EXTERIOR_OFFSET_RATIO
            )
            addRoundRect(
                roundRect = RoundRect(
                    rect = Rect(
                        offset = topLeft + innerRectOffset,
                        size = finderPatternSize * INTERIOR_EXTERIOR_SHAPE_RATIO
                    ),
                    cornerRadius = cornerRadius * 0.12f
                )
            )
        },
        paint = foregroundPaint
    )
}
