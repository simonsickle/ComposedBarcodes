package com.simonsickle.compose.barcodes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Preview composables for the Barcode component to enable snapshot testing.
 */

@Preview(name = "QR Code Default")
@Composable
fun BarcodeQrCodePreview() {
    MaterialTheme(colors = lightColors()) {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.background(Color.White).padding(16.dp)) {
                Barcode(
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp),
                    type = BarcodeType.QR_CODE,
                    value = "https://github.com/simonsickle/composed-barcodes"
                )
                Text("QR Code")
            }
        }
    }
}

@Preview(name = "Code128 Barcode")
@Composable
fun BarcodeCode128Preview() {
    MaterialTheme(colors = lightColors()) {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.background(Color.White).padding(16.dp)) {
                Barcode(
                    modifier = Modifier
                        .width(300.dp)
                        .height(100.dp),
                    type = BarcodeType.CODE_128,
                    value = "123456789012"
                )
                Text("Code 128")
            }
        }
    }
}

@Preview(name = "EAN13 Barcode")
@Composable
fun BarcodeEan13Preview() {
    MaterialTheme(colors = lightColors()) {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.background(Color.White).padding(16.dp)) {
                Barcode(
                    modifier = Modifier
                        .width(300.dp)
                        .height(100.dp),
                    type = BarcodeType.EAN_13,
                    value = "978020137962"
                )
                Text("EAN-13")
            }
        }
    }
}

@Preview(name = "UPC-A Barcode")
@Composable
fun BarcodeUpcAPreview() {
    MaterialTheme(colors = lightColors()) {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.background(Color.White).padding(16.dp)) {
                Barcode(
                    modifier = Modifier
                        .width(300.dp)
                        .height(100.dp),
                    type = BarcodeType.UPC_A,
                    value = "012345678905"
                )
                Text("UPC-A")
            }
        }
    }
}

@Preview(name = "Data Matrix")
@Composable
fun BarcodeDataMatrixPreview() {
    MaterialTheme(colors = lightColors()) {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.background(Color.White).padding(16.dp)) {
                Barcode(
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp),
                    type = BarcodeType.DATA_MATRIX,
                    value = "Data Matrix Test"
                )
                Text("Data Matrix")
            }
        }
    }
}

@Preview(name = "Aztec Code")
@Composable
fun BarcodeAztecPreview() {
    MaterialTheme(colors = lightColors()) {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.background(Color.White).padding(16.dp)) {
                Barcode(
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp),
                    type = BarcodeType.AZTEC,
                    value = "Aztec Code Test"
                )
                Text("Aztec Code")
            }
        }
    }
}

@Preview(name = "PDF417 Barcode")
@Composable
fun BarcodePdf417Preview() {
    MaterialTheme(colors = lightColors()) {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.background(Color.White).padding(16.dp)) {
                Barcode(
                    modifier = Modifier
                        .width(300.dp)
                        .height(100.dp),
                    type = BarcodeType.PDF_417,
                    value = "PDF417 Test Data"
                )
                Text("PDF417")
            }
        }
    }
}

@Preview(name = "Barcode Without Progress Indicator")
@Composable
fun BarcodeNoProgressPreview() {
    MaterialTheme(colors = lightColors()) {
        Surface(color = MaterialTheme.colors.background) {
            Box(modifier = Modifier.background(Color.White).padding(16.dp)) {
                Barcode(
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp)
                        .align(Alignment.Center),
                    type = BarcodeType.QR_CODE,
                    value = "No Progress Test",
                    showProgress = false
                )
            }
        }
    }
}

@Preview(name = "High Resolution Barcode")
@Composable
fun BarcodeHighResolutionPreview() {
    MaterialTheme(colors = lightColors()) {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.background(Color.White).padding(16.dp)) {
                Barcode(
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp),
                    type = BarcodeType.QR_CODE,
                    value = "High Resolution Test",
                    resolutionFactor = 4
                )
                Text("High Resolution (4x)")
            }
        }
    }
}

@Preview(name = "Small QR Code")
@Composable
fun BarcodeSmallPreview() {
    MaterialTheme(colors = lightColors()) {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.background(Color.White).padding(16.dp)) {
                Barcode(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp),
                    type = BarcodeType.QR_CODE,
                    value = "Small",
                    width = 100.dp,
                    height = 100.dp
                )
                Text("Small QR Code")
            }
        }
    }
}

@Preview(name = "Code39 Barcode")
@Composable
fun BarcodeCode39Preview() {
    MaterialTheme(colors = lightColors()) {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.background(Color.White).padding(16.dp)) {
                Barcode(
                    modifier = Modifier
                        .width(300.dp)
                        .height(100.dp),
                    type = BarcodeType.CODE_39,
                    value = "CODE39TEST"
                )
                Text("Code 39")
            }
        }
    }
}
