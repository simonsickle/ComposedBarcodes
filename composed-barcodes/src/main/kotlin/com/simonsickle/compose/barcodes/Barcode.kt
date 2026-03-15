package com.simonsickle.compose.barcodes

import android.graphics.Bitmap
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.floor
import kotlin.math.roundToInt

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
        BarcodeRequest(
            type = type,
            value = value,
            encodeHints = encodeHints
        )
    }

    val uiState by produceState<BarcodeUiState>(
        initialValue = BarcodeUiState.Loading,
        request
    ) {
        BarcodeRenderCache.get(request.cacheKey)?.let { cached ->
            this.value = BarcodeUiState.Ready(cached)
            return@produceState
        }

        val renderedBarcode = withContext(Dispatchers.Default) {
            BarcodePipeline.encodeAndRender(request)
        }

        if (renderedBarcode != null) {
            BarcodeRenderCache.put(request.cacheKey, renderedBarcode)
            this.value = BarcodeUiState.Ready(renderedBarcode)
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

    // If the barcode is not null, draw it. If it is still encoding, show a spinner.
    Box(modifier = modifier) {
        when (val state = uiState) {
            is BarcodeUiState.Ready -> {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawBarcode(state.renderData)
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

private sealed interface BarcodeUiState {
    data object Loading : BarcodeUiState
    data object Error : BarcodeUiState
    data class Ready(val renderData: BarcodeRenderData) : BarcodeUiState
}

private data class BarcodeRequest(
    val type: BarcodeType,
    val value: String,
    val encodeHints: BarcodeEncodeHints
) {
    val cacheKey: BarcodeCacheKey
        get() = BarcodeCacheKey(type, value, encodeHints)
}

private data class BarcodeRenderData(
    val matrixWidth: Int,
    val matrixHeight: Int,
    val requiresSquareModules: Boolean,
    val image: ImageBitmap,
    val byteCount: Int
)

private object BarcodePipeline {
    fun encodeAndRender(request: BarcodeRequest): BarcodeRenderData? = try {
        request.type.getIntrinsicBitMatrix(
            value = request.value,
            encodeHints = request.encodeHints
        ).toRenderData(requiresSquareModules = request.type.requiresSquareModules)
    } catch (e: Exception) {
        Log.e("ComposeBarcodes", "Invalid Barcode Format", e)
        null
    }
}

private fun BitMatrix.toRenderData(requiresSquareModules: Boolean): BarcodeRenderData =
    BarcodeRenderData(
        matrixWidth = width,
        matrixHeight = height,
        requiresSquareModules = requiresSquareModules,
        image = toImageBitmap(),
        byteCount = width * height * BYTES_PER_PIXEL_ARGB_8888
    )

private fun BitMatrix.toImageBitmap(): ImageBitmap {
    val pixels = IntArray(width * height)
    var index = 0

    for (row in 0 until height) {
        for (column in 0 until width) {
            pixels[index] = if (get(column, row)) BLACK_PIXEL_ARGB else TRANSPARENT_PIXEL_ARGB
            index++
        }
    }

    return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888).asImageBitmap()
}

private data class BarcodeCacheKey(
    val type: BarcodeType,
    val value: String,
    val encodeHints: BarcodeEncodeHints
)

private object BarcodeRenderCache {
    private const val MAX_CACHE_SIZE_BYTES = 2 * 1024 * 1024
    private val entries = LinkedHashMap<BarcodeCacheKey, BarcodeRenderData>(
        16,
        0.75f,
        true
    )
    private var currentSizeBytes = 0

    fun get(key: BarcodeCacheKey): BarcodeRenderData? = synchronized(entries) {
        entries[key]
    }

    fun put(key: BarcodeCacheKey, value: BarcodeRenderData) = synchronized(entries) {
        entries.put(key, value)?.let { previous ->
            currentSizeBytes -= previous.byteCount
        }
        currentSizeBytes += value.byteCount
        trimToSize()
    }

    private fun trimToSize() {
        val iterator = entries.entries.iterator()
        while (currentSizeBytes > MAX_CACHE_SIZE_BYTES && iterator.hasNext()) {
            val entry = iterator.next()
            currentSizeBytes -= entry.value.byteCount
            iterator.remove()
        }
    }
}

private const val PROGRESS_VISIBILITY_DELAY_MS = 120L
private const val BYTES_PER_PIXEL_ARGB_8888 = 4
private const val BLACK_PIXEL_ARGB = -0x1000000
private const val TRANSPARENT_PIXEL_ARGB = 0

private fun DrawScope.drawBarcode(barcode: BarcodeRenderData) {
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

    val barcodeWidth = (moduleWidth * barcode.matrixWidth).roundToInt().coerceAtLeast(1)
    val barcodeHeight = (moduleHeight * barcode.matrixHeight).roundToInt().coerceAtLeast(1)
    val offsetX = ((size.width - barcodeWidth) / 2f).roundToInt()
    val offsetY = ((size.height - barcodeHeight) / 2f).roundToInt()

    drawImage(
        image = barcode.image,
        srcOffset = IntOffset.Zero,
        srcSize = IntSize(barcode.matrixWidth, barcode.matrixHeight),
        dstOffset = IntOffset(offsetX, offsetY),
        dstSize = IntSize(barcodeWidth, barcodeHeight),
        filterQuality = FilterQuality.None
    )
}
