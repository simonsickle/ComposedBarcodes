package com.simonsickle.composedbarcodes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simonsickle.compose.barcodes.BarcodeType
import com.simonsickle.composedbarcodes.examples.Aztec
import com.simonsickle.composedbarcodes.examples.Codabar
import com.simonsickle.composedbarcodes.examples.Code128
import com.simonsickle.composedbarcodes.examples.Code39
import com.simonsickle.composedbarcodes.examples.Code93
import com.simonsickle.composedbarcodes.examples.DataMatrix
import com.simonsickle.composedbarcodes.examples.Ean13
import com.simonsickle.composedbarcodes.examples.Ean8
import com.simonsickle.composedbarcodes.examples.Itf
import com.simonsickle.composedbarcodes.examples.Pdf417
import com.simonsickle.composedbarcodes.examples.Qr
import com.simonsickle.composedbarcodes.examples.UpcA
import com.simonsickle.composedbarcodes.examples.UpcE

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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
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
}
