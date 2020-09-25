package com.simonsickle.composed_barcodes.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.simonsickle.composed_barcodes.zxing.BarcodeType

@Composable
fun Barcode(width: Int, height: Int, type: BarcodeType, value: String) {
    Image(type.getBitmap(width = width, height = height, value = value))
}

@Preview(showBackground = true)
@Composable
fun PreviewBarcodes() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for (type in BarcodeType.values()) {
            Spacer(modifier = Modifier.padding(4.dp))
            Barcode(
                width = 50.dp.value.toInt(),
                height = 50.dp.value.toInt(),
                type = type,
                value = type.example
            )
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}

