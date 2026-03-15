package com.simonsickle.compose.barcodes

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.Dispatchers
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
    val barcodeBitmap = remember { mutableStateOf<ImageBitmap?>(null) }

    // Read dimensions from layout constraints so callers control size via modifier.
    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.roundToPx() }
        val heightPx = with(density) { maxHeight.roundToPx() }

        // The launched effect runs whenever inputs or available draw size change.
        LaunchedEffect(value, type, widthPx, heightPx, encodeHints) {
            if (widthPx <= 0 || heightPx <= 0) {
                barcodeBitmap.value = null
                return@LaunchedEffect
            }

            barcodeBitmap.value = withContext(Dispatchers.Default) {
                try {
                    type.getImageBitmap(
                        width = widthPx,
                        height = heightPx,
                        value = value,
                        encodeHints = encodeHints
                    )
                } catch (e: Exception) {
                    Log.e("ComposeBarcodes", "Invalid Barcode Format", e)
                    null
                }
            }
        }

        // If the barcode is not null, display it. If it is null, show a progress spinner.
        Box(modifier = Modifier.fillMaxSize()) {
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
}
