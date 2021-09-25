package com.simonsickle.composedbarcodes.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.simonsickle.compose.barcodes.BarcodeType

private const val BARCODE_VALUE = "01234565"

@Preview
@Composable
fun UpcE() {
    GenericBarcodeExample(barcodeType = BarcodeType.UPC_E, value = BARCODE_VALUE)
}
