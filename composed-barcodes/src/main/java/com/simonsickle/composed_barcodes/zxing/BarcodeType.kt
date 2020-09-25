package com.simonsickle.composed_barcodes.zxing

import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.asImageAsset
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter

enum class BarcodeType(private val barcodeFormat: BarcodeFormat, internal val example: String) {
    EAN_8(BarcodeFormat.EAN_8, "1234567"),
    UPC_E(BarcodeFormat.UPC_E, "04252614"),
    EAN_13(BarcodeFormat.EAN_13, "123456789123"),
    UPC_A(BarcodeFormat.UPC_A, "042100005264"),
    QR_CODE(BarcodeFormat.QR_CODE, "12345678"),
    CODE_39(BarcodeFormat.CODE_39, "1234567890"),
    CODE_93(BarcodeFormat.CODE_93, "1234567890"),
    CODE_128(BarcodeFormat.CODE_128, "1235434235"),
    ITF(BarcodeFormat.ITF, "22114455"),
    PDF_417(BarcodeFormat.PDF_417, "34099439569343f"),
    CODABAR(BarcodeFormat.CODABAR, "23245255356"),
    DATA_MATRIX(BarcodeFormat.DATA_MATRIX, "32541523"),
    AZTEC(BarcodeFormat.AZTEC, "23254212");

    internal fun getBitmap(width: Int, height: Int, value: String): ImageAsset {
        return MultiFormatWriter().encode(value, barcodeFormat, width, height)
            .toBitmap()
            .asImageAsset()
    }
}