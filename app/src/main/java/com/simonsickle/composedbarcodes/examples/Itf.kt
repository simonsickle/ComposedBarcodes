package com.simonsickle.composedbarcodes.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.simonsickle.compose.barcodes.BarcodeType

private const val BARCODE_VALUE = "05012345678900"

@Preview
@Composable
fun Itf() {
    GenericBarcodeExample(barcodeType = BarcodeType.ITF, value = BARCODE_VALUE)
}
