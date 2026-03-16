package com.simonsickle.compose.barcodes

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BarcodePipelineBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun pipeline_ean8() = benchmarkPipeline(BarcodeBenchmarkCases.ean8)

    @Test
    fun pipeline_upce() = benchmarkPipeline(BarcodeBenchmarkCases.upcE)

    @Test
    fun pipeline_ean13() = benchmarkPipeline(BarcodeBenchmarkCases.ean13)

    @Test
    fun pipeline_upca() = benchmarkPipeline(BarcodeBenchmarkCases.upcA)

    @Test
    fun pipeline_qrCode() = benchmarkPipeline(BarcodeBenchmarkCases.qrCode)

    @Test
    fun pipeline_code39() = benchmarkPipeline(BarcodeBenchmarkCases.code39)

    @Test
    fun pipeline_code93() = benchmarkPipeline(BarcodeBenchmarkCases.code93)

    @Test
    fun pipeline_code128() = benchmarkPipeline(BarcodeBenchmarkCases.code128)

    @Test
    fun pipeline_itf() = benchmarkPipeline(BarcodeBenchmarkCases.itf)

    @Test
    fun pipeline_pdf417() = benchmarkPipeline(BarcodeBenchmarkCases.pdf417)

    @Test
    fun pipeline_codabar() = benchmarkPipeline(BarcodeBenchmarkCases.codabar)

    @Test
    fun pipeline_dataMatrix() = benchmarkPipeline(BarcodeBenchmarkCases.dataMatrix)

    @Test
    fun pipeline_aztec() = benchmarkPipeline(BarcodeBenchmarkCases.aztec)

    private fun benchmarkPipeline(benchmarkCase: BarcodeBenchmarkCase) {
        val drawScope = CanvasDrawScope()
        val density = Density(1f)
        val bitmap = ImageBitmap(benchmarkCase.width, benchmarkCase.height)
        val canvas = Canvas(bitmap)
        val renderSize = Size(benchmarkCase.width.toFloat(), benchmarkCase.height.toFloat())

        benchmarkRule.measureRepeated {
            val matrix = benchmarkCase.type.getIntrinsicBitMatrix(benchmarkCase.value)
            val renderPlan = buildRenderPlan(matrix)
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
    }
}
