package com.simonsickle.compose.barcodes

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
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
    val cacheKey = remember(type, value, encodeHints) {
        BarcodeCacheKey(
            type = type,
            value = value,
            encodeHints = encodeHints
        )
    }

    val renderData = remember { mutableStateOf<BarcodeRenderData?>(null) }
    val isEncoding = remember { mutableStateOf(false) }
    val showDelayedProgress = remember { mutableStateOf(false) }

    // Encode only from content inputs. Layout size changes now only affect drawing scale.
    LaunchedEffect(cacheKey) {
        BarcodeRenderCache.get(cacheKey)?.let { cached ->
            renderData.value = cached
            isEncoding.value = false
            return@LaunchedEffect
        }

        isEncoding.value = true
        val encodedRenderData = withContext(Dispatchers.Default) {
            try {
                type.getIntrinsicBitMatrix(
                    value = value,
                    encodeHints = encodeHints
                ).toRenderData(requiresSquareModules = type.requiresSquareModules)
            } catch (e: Exception) {
                Log.e("ComposeBarcodes", "Invalid Barcode Format", e)
                null
            }
        }

        if (encodedRenderData != null) {
            BarcodeRenderCache.put(cacheKey, encodedRenderData)
            renderData.value = encodedRenderData
        } else {
            renderData.value = null
        }

        isEncoding.value = false
    }

    // Avoid flashing the spinner for quick encodes.
    LaunchedEffect(isEncoding.value, renderData.value) {
        if (isEncoding.value && renderData.value == null) {
            delay(PROGRESS_VISIBILITY_DELAY_MS)
            showDelayedProgress.value = isEncoding.value && renderData.value == null
        } else {
            showDelayedProgress.value = false
        }
    }

    // If the barcode is not null, draw it. If it is still encoding, show a spinner.
    Box(modifier = modifier) {
        val barcode = renderData.value
        if (barcode != null) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val baseModuleWidth = floor(size.width / barcode.matrixWidth.toFloat())
                val baseModuleHeight = floor(size.height / barcode.matrixHeight.toFloat())
                val moduleWidth: Float
                val moduleHeight: Float

                if (barcode.requiresSquareModules) {
                    val squareModuleSize = minOf(baseModuleWidth, baseModuleHeight).coerceAtLeast(1f)
                    moduleWidth = squareModuleSize
                    moduleHeight = squareModuleSize
                } else {
                    moduleWidth = baseModuleWidth.coerceAtLeast(1f)
                    moduleHeight = baseModuleHeight.coerceAtLeast(1f)
                }

                val barcodeWidth = moduleWidth * barcode.matrixWidth
                val barcodeHeight = moduleHeight * barcode.matrixHeight
                val offsetX = (size.width - barcodeWidth) / 2f
                val offsetY = (size.height - barcodeHeight) / 2f

                barcode.spans.forEach { span ->
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(
                            x = offsetX + span.startColumn * moduleWidth,
                            y = offsetY + span.row * moduleHeight
                        ),
                        size = Size(
                            width = (span.endColumnExclusive - span.startColumn) * moduleWidth,
                            height = moduleHeight
                        )
                    )
                }
            }
        } else if (showProgress && showDelayedProgress.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .align(Alignment.Center)
            )
        }
    }
}

private data class BarcodeRenderData(
    val matrixWidth: Int,
    val matrixHeight: Int,
    val requiresSquareModules: Boolean,
    val spans: List<BarcodeSpan>
)

private data class BarcodeSpan(
    val row: Int,
    val startColumn: Int,
    val endColumnExclusive: Int
)

private fun BitMatrix.toRenderData(requiresSquareModules: Boolean): BarcodeRenderData =
    BarcodeRenderData(
        matrixWidth = width,
        matrixHeight = height,
        requiresSquareModules = requiresSquareModules,
        spans = toHorizontalSpans()
    )

private fun BitMatrix.toHorizontalSpans(): List<BarcodeSpan> {
    val spans = ArrayList<BarcodeSpan>()

    for (row in 0 until height) {
        var column = 0
        while (column < width) {
            while (column < width && !get(column, row)) {
                column++
            }
            if (column >= width) continue

            val runStart = column
            while (column < width && get(column, row)) {
                column++
            }
            spans += BarcodeSpan(
                row = row,
                startColumn = runStart,
                endColumnExclusive = column
            )
        }
    }

    return spans
}

private data class BarcodeCacheKey(
    val type: BarcodeType,
    val value: String,
    val encodeHints: BarcodeEncodeHints
)

private object BarcodeRenderCache {
    private const val MAX_CACHE_ENTRIES = 32
    private val entries = LinkedHashMap<BarcodeCacheKey, BarcodeRenderData>(
        MAX_CACHE_ENTRIES,
        0.75f,
        true
    )

    fun get(key: BarcodeCacheKey): BarcodeRenderData? = synchronized(entries) {
        entries[key]
    }

    fun put(key: BarcodeCacheKey, value: BarcodeRenderData) = synchronized(entries) {
        entries[key] = value
        if (entries.size > MAX_CACHE_ENTRIES) {
            val oldestKey = entries.entries.firstOrNull()?.key ?: return@synchronized
            entries.remove(oldestKey)
        }
    }
}

private const val PROGRESS_VISIBILITY_DELAY_MS = 120L
