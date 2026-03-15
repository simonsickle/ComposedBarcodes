package com.simonsickle.composedbarcodes.examples

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

/**
 * Paparazzi snapshot tests for all barcode preview composables.
 * These tests ensure that the UI renders correctly and catches any visual regressions.
 */
class BarcodePreviewSnapshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        showSystemUi = false
    )

    @Test
    fun qrCodePreview() {
        paparazzi.snapshot {
            Qr()
        }
    }

    @Test
    fun aztecPreview() {
        paparazzi.snapshot {
            Aztec()
        }
    }

    @Test
    fun code39Preview() {
        paparazzi.snapshot {
            Code39()
        }
    }

    @Test
    fun code93Preview() {
        paparazzi.snapshot {
            Code93()
        }
    }

    @Test
    fun code128Preview() {
        paparazzi.snapshot {
            Code128()
        }
    }

    @Test
    fun codabarPreview() {
        paparazzi.snapshot {
            Codabar()
        }
    }

    @Test
    fun dataMatrixPreview() {
        paparazzi.snapshot {
            DataMatrix()
        }
    }

    @Test
    fun ean8Preview() {
        paparazzi.snapshot {
            Ean8()
        }
    }

    @Test
    fun ean13Preview() {
        paparazzi.snapshot {
            Ean13()
        }
    }

    @Test
    fun itfPreview() {
        paparazzi.snapshot {
            Itf()
        }
    }

    @Test
    fun pdf417Preview() {
        paparazzi.snapshot {
            Pdf417()
        }
    }

    @Test
    fun upcAPreview() {
        paparazzi.snapshot {
            UpcA()
        }
    }

    @Test
    fun upcEPreview() {
        paparazzi.snapshot {
            UpcE()
        }
    }
}
