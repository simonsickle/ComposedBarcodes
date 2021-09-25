package com.simonsickle.composedbarcodes

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.simonsickle.composedbarcodes.ui.ComposedBarcodesTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposedBarcodesTheme {
                NavigationRegistry()
            }
        }
    }
}
