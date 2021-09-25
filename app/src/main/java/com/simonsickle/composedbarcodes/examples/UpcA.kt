package com.simonsickle.composedbarcodes.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.simonsickle.compose.barcodes.BarcodeType

private const val BARCODE_VALUE = "828999006823"

@Preview
@Composable
fun UpcA() {
    GenericBarcodeExample(barcodeType = BarcodeType.UPC_A, value = BARCODE_VALUE)
}
