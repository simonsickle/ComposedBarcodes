package com.simonsickle.compose.barcodes

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Barcode asynchronously creates a barcode bitmap in the background and then displays
 * the barcode via an Image composable. A progress indicator shows, optionally, until
 * the barcode value has been encoded to a bitmap.
 *
 * Note: if the barcode is not a valid format, the spinner will continue forever.
 *
 * @param modifier the modifier to be applied to the layout
 * @param showProgress true will show the progress indicator. Defaults to true.
 * @param resolutionFactor multiplied on the width/height to get the resolution, in px, for the bitmap
 * @param width for the generated bitmap multiplied by the resolutionFactor
 * @param height for the generated bitmap multiplied by the resolutionFactor
 * @param type the type of barcode to render
 * @param value the value of the barcode to show
 * @param encodeHints immutable ZXing encode hints wrapper (for example, setting CHARACTER_SET)
 */
@Composable
fun Barcode(
    modifier: Modifier = Modifier,
    showProgress: Boolean = true,
    resolutionFactor: Int = 1,
    width: Dp = 128.dp,
    height: Dp = 128.dp,
    type: BarcodeType,
    value: String,
    encodeHints: BarcodeEncodeHints = BarcodeEncodeHints.None
) {
    val barcodeBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    val scope = rememberCoroutineScope()

    // The launched effect will run every time the value changes. So, if the barcode changes,
    // the coroutine to get the bitmap will be started.
    LaunchedEffect(value, type, width, height, resolutionFactor, encodeHints) {
        scope.launch {
            withContext(Dispatchers.Default) {
                barcodeBitmap.value = try {
                    type.getImageBitmap(
                        width = (width.value * resolutionFactor).toInt(),
                        height = (height.value * resolutionFactor).toInt(),
                        value = value,
                        encodeHints = encodeHints
                    )
                } catch (e: Exception) {
                    Log.e("ComposeBarcodes", "Invalid Barcode Format", e)
                    null
                }
            }
        }
    }

    // Contain the barcode in a box that matches the provided dimensions
    Box(modifier = modifier) {
        // If the barcode is not null, display it. If it is null, then the code hasn't
        // completed the draw in the background so show a progress spinner in place.
        barcodeBitmap.value?.let { barcode ->
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = BitmapPainter(barcode),
                contentDescription = value
            )
        } ?: run {
            if (showProgress) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize(0.5f)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

/**
 * SynchronousBarcode generates a barcode bitmap synchronously during composition.
 * This version is intended for snapshot testing with Paparazzi where asynchronous
 * operations are not suitable. For production use, prefer the async [Barcode] composable.
 *
 * @param modifier the modifier to be applied to the layout
 * @param resolutionFactor multiplied on the width/height to get the resolution, in px, for the bitmap
 * @param width for the generated bitmap multiplied by the resolutionFactor
 * @param height for the generated bitmap multiplied by the resolutionFactor
 * @param type the type of barcode to render
 * @param value the value of the barcode to show
 * @param encodeHints immutable ZXing encode hints wrapper (for example, setting CHARACTER_SET)
 */
@Composable
fun SynchronousBarcode(
    modifier: Modifier = Modifier,
    resolutionFactor: Int = 1,
    width: Dp = 128.dp,
    height: Dp = 128.dp,
    type: BarcodeType,
    value: String,
    encodeHints: BarcodeEncodeHints = BarcodeEncodeHints.None
) {
    val barcodeBitmap = remember(value, type, width, height, resolutionFactor, encodeHints) {
        try {
            type.getImageBitmap(
                width = (width.value * resolutionFactor).toInt(),
                height = (height.value * resolutionFactor).toInt(),
                value = value,
                encodeHints = encodeHints
            )
        } catch (e: Exception) {
            Log.e("ComposeBarcodes", "Invalid Barcode Format", e)
            null
        }
    }

    Box(modifier = modifier) {
        barcodeBitmap?.let { barcode ->
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = BitmapPainter(barcode),
                contentDescription = value
            )
        }
    }
}
