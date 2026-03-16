package com.simonsickle.compose.barcodes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.zxing.common.BitMatrix
import kotlin.math.floor
import kotlin.system.measureNanoTime
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BarcodePerformanceVerificationTest {

    @Test
    fun verifyGenerationAndRenderPerformance() {
        val drawScope = CanvasDrawScope()
        val density = Density(1f)

        for (benchmarkCase in BarcodeBenchmarkCases.all) {
            val generationSamples = sampleTimes {
                benchmarkCase.type.getIntrinsicBitMatrix(benchmarkCase.value)
            }
            val generationMedianMs = generationSamples.medianNanos().toMillis()
            assertTrue(
                "${benchmarkCase.type} generation regression: $generationMedianMs ms",
                generationMedianMs < MAX_GENERATION_TIME_MS
            )

            val matrix = benchmarkCase.type.getIntrinsicBitMatrix(benchmarkCase.value)
            val renderPlan = buildRenderPlan(matrix)
            val bitmap = ImageBitmap(benchmarkCase.width, benchmarkCase.height)
            val canvas = Canvas(bitmap)
            val renderSize = Size(benchmarkCase.width.toFloat(), benchmarkCase.height.toFloat())

            val optimizedRenderSamples = sampleTimes {
                drawScope.draw(
                    density = density,
                    layoutDirection = LayoutDirection.Ltr,
                    canvas = canvas,
                    size = renderSize
                ) {
                    drawBitMatrix(
                        renderPlan = renderPlan,
                        requiresSquareModules = benchmarkCase.type.requiresSquareModules
                    )
                }
            }

            val legacyRenderSamples = sampleTimes {
                drawScope.draw(
                    density = density,
                    layoutDirection = LayoutDirection.Ltr,
                    canvas = canvas,
                    size = renderSize
                ) {
                    drawBitMatrixLegacy(
                        matrix = matrix,
                        requiresSquareModules = benchmarkCase.type.requiresSquareModules
                    )
                }
            }

            val optimizedRenderMedian = optimizedRenderSamples.medianNanos()
            val legacyRenderMedian = legacyRenderSamples.medianNanos()
            val ratio = optimizedRenderMedian.toDouble() / legacyRenderMedian.toDouble()
            assertTrue(
                "${benchmarkCase.type} render regression. optimized/legacy ratio=$ratio",
                ratio <= MAX_RENDER_RATIO_VS_LEGACY
            )
        }
    }
}

private fun sampleTimes(
    warmupIterations: Int = 5,
    measuredIterations: Int = 15,
    block: () -> Unit
): LongArray {
    repeat(warmupIterations) {
        block()
    }
    return LongArray(measuredIterations) {
        measureNanoTime(block)
    }
}

private fun LongArray.medianNanos(): Long {
    val sorted = sortedArray()
    return sorted[sorted.size / 2]
}

private fun Long.toMillis(): Double = this / 1_000_000.0

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBitMatrixLegacy(
    matrix: BitMatrix,
    requiresSquareModules: Boolean
) {
    val matrixWidth = matrix.width
    val matrixHeight = matrix.height
    if (matrixWidth <= 0 || matrixHeight <= 0) return

    val baseModuleWidth = floor(size.width / matrixWidth.toFloat())
    val baseModuleHeight = floor(size.height / matrixHeight.toFloat())
    val moduleWidth: Float
    val moduleHeight: Float

    if (requiresSquareModules) {
        val squareModuleSize = minOf(baseModuleWidth, baseModuleHeight).coerceAtLeast(1f)
        moduleWidth = squareModuleSize
        moduleHeight = squareModuleSize
    } else {
        moduleWidth = baseModuleWidth.coerceAtLeast(1f)
        moduleHeight = baseModuleHeight.coerceAtLeast(1f)
    }

    val barcodeWidth = moduleWidth * matrixWidth
    val barcodeHeight = moduleHeight * matrixHeight
    val offsetX = (size.width - barcodeWidth) / 2f
    val offsetY = (size.height - barcodeHeight) / 2f

    for (row in 0 until matrixHeight) {
        var column = 0
        while (column < matrixWidth) {
            while (column < matrixWidth && !matrix.get(column, row)) {
                column++
            }
            if (column >= matrixWidth) continue

            val runStart = column
            while (column < matrixWidth && matrix.get(column, row)) {
                column++
            }

            drawRect(
                color = Color.Black,
                topLeft = Offset(
                    x = offsetX + runStart * moduleWidth,
                    y = offsetY + row * moduleHeight
                ),
                size = Size(
                    width = (column - runStart) * moduleWidth,
                    height = moduleHeight
                )
            )
        }
    }
}

private const val MAX_GENERATION_TIME_MS = 100.0
private const val MAX_RENDER_RATIO_VS_LEGACY = 1.05
