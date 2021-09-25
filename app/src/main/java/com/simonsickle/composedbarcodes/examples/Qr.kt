package com.simonsickle.composedbarcodes.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.simonsickle.compose.barcodes.BarcodeType

private const val BARCODE_VALUE = "https://github.com/simonsickle"

@Preview
@Composable
fun Qr() {
    GenericBarcodeExample(barcodeType = BarcodeType.QR_CODE, value = BARCODE_VALUE)
}
