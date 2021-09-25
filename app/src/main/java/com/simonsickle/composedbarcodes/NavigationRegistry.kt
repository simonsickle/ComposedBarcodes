package com.simonsickle.composedbarcodes

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simonsickle.compose.barcodes.BarcodeType
import com.simonsickle.composedbarcodes.examples.*

@Composable
fun NavigationRegistry() {
    val title = remember { mutableStateOf("Barcode List") }
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (title.value != "Barcode List") {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Filled.ArrowBack, "Back")
                        }
                    }
                },
                title = {
                    Text(
                        text = title.value,
                        style = MaterialTheme.typography.h5,
                    )
                })
        }
    ) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                title.value = "Barcode List"
                BarcodeList(navController = navController)
            }
            composable(BarcodeType.AZTEC.name) {
                title.value = "Aztec"
                Aztec()
            }
            composable(BarcodeType.CODABAR.name) {
                title.value = "Codabar"
                Codabar()
            }
            composable(BarcodeType.CODE_128.name) {
                title.value = "Code128"
                Code128()
            }
            composable(BarcodeType.CODE_39.name) {
                title.value = "Code39"
                Code39()
            }
            composable(BarcodeType.CODE_93.name) {
                title.value = "Code93"
                Code93()
            }
            composable(BarcodeType.DATA_MATRIX.name) {
                title.value = "DataMatrix"
                DataMatrix()
            }
            composable(BarcodeType.EAN_13.name) {
                title.value = "Ean13"
                Ean13()
            }
            composable(BarcodeType.EAN_8.name) {
                title.value = "Ean8"
                Ean8()
            }
            composable(BarcodeType.ITF.name) {
                title.value = "Itf"
                Itf()
            }
            composable(BarcodeType.PDF_417.name) {
                title.value = "Pdf 417"
                Pdf417()
            }
            composable(BarcodeType.QR_CODE.name) {
                title.value = "QR Code"
                Qr()
            }
            composable(BarcodeType.UPC_A.name) {
                title.value = "UPC A"
                UpcA()
            }
            composable(BarcodeType.UPC_E.name) {
                title.value = "UPC E"
                UpcE()
            }
        }
    }
}
