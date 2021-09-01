package com.simonsickle.compose.barcodes

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test

class BarcodeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenUsingQrCode_BarcodeIsDisplayed() {
        composeTestRule.setContent {
            Barcode(
                modifier = Modifier.size(300.dp),
                type = BarcodeType.QR_CODE,
                value = "https://google.com",
            )
        }

        composeTestRule.onRoot()
            .assertIsDisplayed()
            .assertWidthIsAtLeast(300.dp)
            .assertHeightIsAtLeast(300.dp)
    }
}