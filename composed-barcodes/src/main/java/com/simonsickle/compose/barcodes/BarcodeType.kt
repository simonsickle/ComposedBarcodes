package com.simonsickle.compose.barcodes

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

enum class BarcodeType(private val barcodeFormat: BarcodeFormat) {
    EAN_8(BarcodeFormat.EAN_8),
    UPC_E(BarcodeFormat.UPC_E),
    EAN_13(BarcodeFormat.EAN_13),
    UPC_A(BarcodeFormat.UPC_A),
    QR_CODE(BarcodeFormat.QR_CODE),
    CODE_39(BarcodeFormat.CODE_39),
    CODE_93(BarcodeFormat.CODE_93),
    CODE_128(BarcodeFormat.CODE_128),
    ITF(BarcodeFormat.ITF),
    PDF_417(BarcodeFormat.PDF_417),
    CODABAR(BarcodeFormat.CODABAR),
    DATA_MATRIX(BarcodeFormat.DATA_MATRIX),
    AZTEC(BarcodeFormat.AZTEC);

    private fun BitMatrix.toBitmap(): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val pixelColor = if (get(x, y)) {
                        android.graphics.Color.BLACK
                    } else {
                        android.graphics.Color.WHITE
                    }
                    setPixel(x, y, pixelColor)
                }
            }
        }
    }

    internal fun getBitmap(width: Int, height: Int, value: String): ImageBitmap {
        return MultiFormatWriter().encode(value, barcodeFormat, width, height)
            .toBitmap()
            .asImageBitmap()
    }

    fun isValueValid(valueToCheck: String): Boolean {
        val barcode = try {
            MultiFormatWriter().encode(valueToCheck, barcodeFormat, 25, 25)
        } catch (e: Exception) {
            null
        }

        return barcode != null
    }
}