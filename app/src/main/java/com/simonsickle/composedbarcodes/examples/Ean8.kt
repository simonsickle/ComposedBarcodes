package com.simonsickle.composedbarcodes.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.simonsickle.compose.barcodes.BarcodeType

private const val BARCODE_VALUE = "23232426"

@Preview
@Composable
fun Ean8() {
    GenericBarcodeExample(barcodeType = BarcodeType.EAN_8, value = BARCODE_VALUE)
}
