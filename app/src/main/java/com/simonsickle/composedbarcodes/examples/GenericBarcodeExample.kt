package com.simonsickle.composedbarcodes.examples

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.simonsickle.compose.barcodes.Barcode
import com.simonsickle.compose.barcodes.BarcodeType

@Composable
fun GenericBarcodeExample(
    barcodeType: BarcodeType,
    value: String,
) {
    Surface(color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (barcodeType.isValueValid(value)) {
                Barcode(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(300.dp)
                        .height(100.dp),
                    type = barcodeType,
                    value = value,
                    width = 300.dp,
                    height = 100.dp,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = value
                )
            }
        }
    }
}
