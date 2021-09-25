package com.simonsickle.composedbarcodes.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.simonsickle.compose.barcodes.BarcodeType

private const val BARCODE_VALUE = "CODE-39P"

@Preview
@Composable
fun Code39() {
    GenericBarcodeExample(barcodeType = BarcodeType.CODE_39, value = BARCODE_VALUE)
}
