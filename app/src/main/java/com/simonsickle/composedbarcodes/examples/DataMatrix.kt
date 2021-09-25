package com.simonsickle.composedbarcodes.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.simonsickle.compose.barcodes.BarcodeType

private const val BARCODE_VALUE = "123456823232"

@Preview
@Composable
fun DataMatrix() {
    GenericBarcodeExample(barcodeType = BarcodeType.DATA_MATRIX, value = BARCODE_VALUE)
}
