package com.simonsickle.compose.barcodes

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

/**
 * Paparazzi snapshot tests for the Barcode composable.
 * These tests verify the visual appearance of different barcode types and configurations.
 * Uses SynchronousBarcode in preview composables to ensure actual barcodes are rendered
 * in snapshots rather than loading states.
 */
class BarcodeSnapshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        showSystemUi = false
    )

    @Test
    fun barcodeQrCode() {
        paparazzi.snapshot {
            BarcodeQrCodePreview()
        }
    }

    @Test
    fun barcodeCode128() {
        paparazzi.snapshot {
            BarcodeCode128Preview()
        }
    }

    @Test
    fun barcodeEan13() {
        paparazzi.snapshot {
            BarcodeEan13Preview()
        }
    }

    @Test
    fun barcodeUpcA() {
        paparazzi.snapshot {
            BarcodeUpcAPreview()
        }
    }

    @Test
    fun barcodeDataMatrix() {
        paparazzi.snapshot {
            BarcodeDataMatrixPreview()
        }
    }

    @Test
    fun barcodeAztec() {
        paparazzi.snapshot {
            BarcodeAztecPreview()
        }
    }

    @Test
    fun barcodePdf417() {
        paparazzi.snapshot {
            BarcodePdf417Preview()
        }
    }

    @Test
    fun barcodeNoProgress() {
        paparazzi.snapshot {
            BarcodeNoProgressPreview()
        }
    }

    @Test
    fun barcodeHighResolution() {
        paparazzi.snapshot {
            BarcodeHighResolutionPreview()
        }
    }

    @Test
    fun barcodeSmall() {
        paparazzi.snapshot {
            BarcodeSmallPreview()
        }
    }

    @Test
    fun barcodeCode39() {
        paparazzi.snapshot {
            BarcodeCode39Preview()
        }
    }
}
