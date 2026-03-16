package com.simonsickle.compose.barcodes

internal data class BarcodeBenchmarkCase(
    val type: BarcodeType,
    val value: String,
    val width: Int,
    val height: Int
)

internal object BarcodeBenchmarkCases {
    val ean8 = BarcodeBenchmarkCase(BarcodeType.EAN_8, "23232426", 640, 240)
    val upcE = BarcodeBenchmarkCase(BarcodeType.UPC_E, "01234565", 640, 240)
    val ean13 = BarcodeBenchmarkCase(BarcodeType.EAN_13, "978020137962", 640, 240)
    val upcA = BarcodeBenchmarkCase(BarcodeType.UPC_A, "828999006823", 640, 240)
    val qrCode = BarcodeBenchmarkCase(BarcodeType.QR_CODE, "https://github.com/simonsickle", 320, 320)
    val code39 = BarcodeBenchmarkCase(BarcodeType.CODE_39, "CODE-39P", 640, 240)
    val code93 = BarcodeBenchmarkCase(BarcodeType.CODE_93, "CODE-39P", 640, 240)
    val code128 = BarcodeBenchmarkCase(BarcodeType.CODE_128, "123456823232", 640, 240)
    val itf = BarcodeBenchmarkCase(BarcodeType.ITF, "05012345678900", 640, 240)
    val pdf417 = BarcodeBenchmarkCase(BarcodeType.PDF_417, "123456823232", 640, 320)
    val codabar = BarcodeBenchmarkCase(BarcodeType.CODABAR, "31117013206375", 640, 240)
    val dataMatrix = BarcodeBenchmarkCase(BarcodeType.DATA_MATRIX, "123456823232", 320, 320)
    val aztec = BarcodeBenchmarkCase(BarcodeType.AZTEC, "123456823232", 320, 320)

    val all = listOf(
        ean8,
        upcE,
        ean13,
        upcA,
        qrCode,
        code39,
        code93,
        code128,
        itf,
        pdf417,
        codabar,
        dataMatrix,
        aztec
    )
}
