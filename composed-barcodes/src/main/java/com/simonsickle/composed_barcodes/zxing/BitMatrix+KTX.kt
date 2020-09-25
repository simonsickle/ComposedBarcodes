package com.simonsickle.composed_barcodes.zxing

import android.graphics.Bitmap
import com.google.zxing.common.BitMatrix

internal fun BitMatrix.toBitmap(): Bitmap {
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