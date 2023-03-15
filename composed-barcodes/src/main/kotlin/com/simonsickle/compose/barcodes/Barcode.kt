package com.simonsickle.compose.barcodes

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.tooling.preview.Preview
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
 * @param resolutionFactor multiplied by 128 to get the resolution, in px, for the bitmap
 * @param type the type of barcode to render
 * @param value the value of the barcode to show
 */
@Composable
fun Barcode(
    modifier: Modifier = Modifier,
    showProgress: Boolean = true,
    resolutionFactor: Int = 10,
    type: BarcodeType,
    value: String
) {
    val barcodeBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    val scope = rememberCoroutineScope()

    // The launched effect will run every time the value changes. So, if the barcode changes,
    // the coroutine to get the bitmap will be started.
    LaunchedEffect(value) {
        scope.launch {
            withContext(Dispatchers.Default) {
                barcodeBitmap.value = try {
                    type.getImageBitmap(
                        width = 128 * resolutionFactor,
                        height = 128 * resolutionFactor,
                        value = value
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
 * Barcode asynchronously creates a barcode bitmap in the background and then displays
 * the barcode via an Image composable. A progress indicator shows, optionally, until
 * the barcode value has been encoded to a bitmap.
 *
 * Note: if the barcode is not a valid format, the spinner will continue forever.
 *
 * @param modifier the modifier to be applied to the layout
 * @param width the width desired for the barcode
 * @param height the height desired for the barcode
 * @param showProgress true will show the progress indicator. Defaults to true.
 * @param type the type of barcode to render
 * @param value the value of the barcode to show
 */
@Deprecated("Please set desired height and width on the modifier")
@Composable
fun Barcode(
    modifier: Modifier = Modifier,
    width: Dp = 50.dp,
    height: Dp = 50.dp,
    showProgress: Boolean = true,
    type: BarcodeType,
    value: String
) {
    Barcode(
        modifier = modifier
            .width(width = width)
            .height(height = height),
        showProgress = showProgress,
        type = type,
        value = value
    )
}