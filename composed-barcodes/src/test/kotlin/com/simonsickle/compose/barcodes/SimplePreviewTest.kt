package com.simonsickle.compose.barcodes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

/**
 * Simple Paparazzi test to verify the setup is working correctly.
 */
class SimplePreviewTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        showSystemUi = false
    )

    @Test
    fun simpleTextPreview() {
        paparazzi.snapshot {
            MaterialTheme(colors = lightColors()) {
                Surface {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Paparazzi Test")
                        Text("Snapshot testing is working!")
                    }
                }
            }
        }
    }
}
