package com.simonsickle.composedbarcodes.examples

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.simonsickle.compose.barcodes.BarcodeType

private const val BARCODE_VALUE = "31117013206375"

@Preview
@Composable
fun Codabar() {
    GenericBarcodeExample(barcodeType = BarcodeType.CODABAR, value = BARCODE_VALUE)
}
