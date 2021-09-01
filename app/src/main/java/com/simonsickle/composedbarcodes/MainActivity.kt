package com.simonsickle.composedbarcodes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simonsickle.compose.barcodes.Barcode
import com.simonsickle.compose.barcodes.BarcodeType
import com.simonsickle.composedbarcodes.ui.ComposedBarcodesTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(ComposeView(this).apply {
            setContent {
                MainActivityContent()
            }
        })
    }

    @Preview
    @Composable
    private fun MainActivityContent() {
        ComposedBarcodesTheme {
            // A surface container using the 'background' color from the theme
            Surface(color = MaterialTheme.colors.background) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // Make sure the value is valid for the type of barcode selected
                    if (BarcodeType.QR_CODE.isValueValid(URL)) {

                        // Display the barcode to the user
                        Barcode(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .width(300.dp)
                                .height(300.dp),
                            type = BarcodeType.QR_CODE,
                            value = URL
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val URL = "https://github.com/simonsickle"
    }
}