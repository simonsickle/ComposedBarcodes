package com.simonsickle.compose.barcodes

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

    internal val requiresSquareModules: Boolean
        get() = this == QR_CODE || this == DATA_MATRIX || this == AZTEC

    private val writer = object : ThreadLocal<MultiFormatWriter>() {
        override fun initialValue(): MultiFormatWriter = MultiFormatWriter()
    }

    internal fun getIntrinsicBitMatrix(
        value: String,
        encodeHints: BarcodeEncodeHints = BarcodeEncodeHints.None
    ): BitMatrix = getWriter().encode(
        value,
        barcodeFormat,
        1,
        1,
        encodeHints.values
    )

    fun isValueValid(
        valueToCheck: String,
        encodeHints: BarcodeEncodeHints = BarcodeEncodeHints.None
    ): Boolean {
        val barcode = try {
            getWriter().encode(valueToCheck, barcodeFormat, 25, 25, encodeHints.values)
        } catch (_: Exception) {
            null
        }

        return barcode != null
    }

    private fun getWriter(): MultiFormatWriter =
        writer.get() ?: MultiFormatWriter().also { writer.set(it) }
}
