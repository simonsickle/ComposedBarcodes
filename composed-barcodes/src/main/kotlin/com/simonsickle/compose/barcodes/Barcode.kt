package com.simonsickle.compose.barcodes

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.floor

/**
 * Barcode asynchronously creates a ZXing matrix in the background and then draws it
 * directly on a Compose Canvas. A progress indicator shows, optionally, while the
 * matrix is being encoded.
 *
 * @param modifier the modifier to be applied to the layout
 * @param showProgress true will show the progress indicator. Defaults to true.
 * @param type the type of barcode to render
 * @param value the value of the barcode to show
 * @param encodeHints immutable ZXing encode hints wrapper (for example, setting CHARACTER_SET)
 */
@Composable
fun Barcode(
    modifier: Modifier = Modifier,
    showProgress: Boolean = true,
    type: BarcodeType,
    value: String,
    encodeHints: BarcodeEncodeHints = BarcodeEncodeHints.None
) {
    val request = remember(type, value, encodeHints) {
        BarcodeRequest(type = type, value = value, encodeHints = encodeHints)
    }

    val uiState by produceState<BarcodeUiState>(
        initialValue = BarcodeUiState.Loading,
        request
    ) {
        BarcodeMatrixCache.get(request.cacheKey)?.let { cached ->
            this.value = BarcodeUiState.Ready(cached)
            return@produceState
        }

        val encoded = withContext(Dispatchers.Default) {
            BarcodeEncoder.encode(request)
        }

        if (encoded != null) {
            BarcodeMatrixCache.put(request.cacheKey, encoded)
            this.value = BarcodeUiState.Ready(encoded)
        } else {
            this.value = BarcodeUiState.Error
        }
    }

    val showDelayedProgress by produceState(
        initialValue = false,
        showProgress,
        uiState
    ) {
        if (showProgress && uiState is BarcodeUiState.Loading) {
            delay(PROGRESS_VISIBILITY_DELAY_MS)
            this.value = uiState is BarcodeUiState.Loading
        } else {
            this.value = false
        }
    }

    Box(modifier = modifier) {
        when (val state = uiState) {
            is BarcodeUiState.Ready -> {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawBitMatrix(
                        renderPlan = state.barcode.renderPlan,
                        requiresSquareModules = state.barcode.requiresSquareModules
                    )
                }
            }
            BarcodeUiState.Loading -> {
                if (showDelayedProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize(0.5f)
                            .align(Alignment.Center)
                    )
                }
            }
            BarcodeUiState.Error -> Unit
        }
    }
}

private data class BarcodeRequest(
    val type: BarcodeType,
    val value: String,
    val encodeHints: BarcodeEncodeHints
) {
    val cacheKey: BarcodeCacheKey
        get() = BarcodeCacheKey(type = type, value = value, encodeHints = encodeHints)
}

private sealed interface BarcodeUiState {
    data object Loading : BarcodeUiState
    data object Error : BarcodeUiState
    data class Ready(val barcode: EncodedBarcode) : BarcodeUiState
}

private data class EncodedBarcode(
    val renderPlan: BarcodeRenderPlan,
    val requiresSquareModules: Boolean,
    val estimatedSizeBytes: Int
)

private object BarcodeEncoder {
    fun encode(request: BarcodeRequest): EncodedBarcode? {
        if (request.value.isBlank()) return null

        val matrix = try {
            request.type.getIntrinsicBitMatrix(
                value = request.value,
                encodeHints = request.encodeHints
            )
        } catch (e: Exception) {
            Log.e("ComposeBarcodes", "Invalid Barcode Format", e)
            return null
        }

        val renderPlan = buildRenderPlan(matrix)

        return EncodedBarcode(
            renderPlan = renderPlan,
            requiresSquareModules = request.type.requiresSquareModules,
            estimatedSizeBytes = estimateRenderPlanSizeBytes(renderPlan)
        )
    }
}

private data class BarcodeCacheKey(
    val type: BarcodeType,
    val value: String,
    val encodeHints: BarcodeEncodeHints
)

private object BarcodeMatrixCache {
    private const val MAX_CACHE_SIZE_BYTES = 512 * 1024
    private val entries = LinkedHashMap<BarcodeCacheKey, EncodedBarcode>(
        16,
        0.75f,
        true
    )
    private var currentSizeBytes = 0

    fun get(key: BarcodeCacheKey): EncodedBarcode? = synchronized(entries) {
        entries[key]
    }

    fun put(key: BarcodeCacheKey, value: EncodedBarcode) = synchronized(entries) {
        entries.put(key, value)?.let { previous ->
            currentSizeBytes -= previous.estimatedSizeBytes
        }
        currentSizeBytes += value.estimatedSizeBytes
        trimToSize()
    }

    private fun trimToSize() {
        val iterator = entries.entries.iterator()
        while (currentSizeBytes > MAX_CACHE_SIZE_BYTES && iterator.hasNext()) {
            val entry = iterator.next()
            currentSizeBytes -= entry.value.estimatedSizeBytes
            iterator.remove()
        }
    }
}

private fun estimateRenderPlanSizeBytes(renderPlan: BarcodeRenderPlan): Int {
    var runBytes = 0
    for (rowRuns in renderPlan.runsByRow) {
        runBytes += rowRuns.size * Int.SIZE_BYTES
    }
    return runBytes + 96
}

internal class BarcodeRenderPlan(
    val width: Int,
    val height: Int,
    val runsByRow: Array<IntArray>
)

internal fun buildRenderPlan(matrix: BitMatrix): BarcodeRenderPlan {
    val width = matrix.width
    val height = matrix.height
    val runsByRow = Array(height) { row ->
        // Worst case is alternating black/white values.
        val runs = IntArray(width + 1)
        var runIndex = 0
        var column = 0
        while (column < width) {
            while (column < width && !matrix.get(column, row)) {
                column++
            }
            if (column >= width) continue

            val runStart = column
            while (column < width && matrix.get(column, row)) {
                column++
            }

            runs[runIndex++] = runStart
            runs[runIndex++] = column
        }
        runs.copyOf(runIndex)
    }

    return BarcodeRenderPlan(
        width = width,
        height = height,
        runsByRow = runsByRow
    )
}

internal fun DrawScope.drawBitMatrix(
    renderPlan: BarcodeRenderPlan,
    requiresSquareModules: Boolean
) {
    val matrixWidth = renderPlan.width
    val matrixHeight = renderPlan.height
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

    // Runs are precomputed off the main thread so drawing can stay lightweight.
    for (row in 0 until matrixHeight) {
        val rowRuns = renderPlan.runsByRow[row]
        var index = 0
        while (index < rowRuns.size) {
            val runStart = rowRuns[index]
            val runEnd = rowRuns[index + 1]
            drawRect(
                color = Color.Black,
                topLeft = Offset(
                    x = offsetX + runStart * moduleWidth,
                    y = offsetY + row * moduleHeight
                ),
                size = Size(
                    width = (runEnd - runStart) * moduleWidth,
                    height = moduleHeight
                )
            )
            index += 2
        }
    }
}

private const val PROGRESS_VISIBILITY_DELAY_MS = 120L
