package com.simonsickle.composedbarcodes.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.simonsickle.compose.barcodes.BarcodeType

private const val BARCODE_VALUE = "978020137962"

@Preview
@Composable
fun Ean13() {
    GenericBarcodeExample(barcodeType = BarcodeType.EAN_13, value = BARCODE_VALUE)
}
