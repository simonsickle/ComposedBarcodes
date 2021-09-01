package com.simonsickle.compose.barcodes

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.junit.Test


class BarcodeTypeTest {
    @Test
    fun whenUsingEan8_ImageIsGeneratedProperly() {
        val bitmap = BarcodeType.EAN_8.getImageBitmap(76, 128, "90311017")
        val binaryBitmap = bitmap.asAndroidBitmap().toBinaryBitmap()

        val decodedBarcode = MultiFormatReader().decode(binaryBitmap)

        assertThat(bitmap).isNotNull()
        assertThat(bitmap.height).isEqualTo(128)
        assertThat(bitmap.width).isEqualTo(76)
        assertThat(decodedBarcode.barcodeFormat).isEqualTo(BarcodeFormat.EAN_8)
        assertThat(decodedBarcode.text).isEqualTo("90311017")
    }

    @Test
    fun whenUsingUpcE_ImageIsGeneratedProperly() {
        val bitmap = BarcodeType.UPC_E.getImageBitmap(76, 128, "01234565")
        val binaryBitmap = bitmap.asAndroidBitmap().toBinaryBitmap()

        val decodedBarcode = MultiFormatReader().decode(binaryBitmap)

        assertThat(bitmap).isNotNull()
        assertThat(bitmap.height).isEqualTo(128)
        assertThat(bitmap.width).isEqualTo(76)
        assertThat(decodedBarcode.barcodeFormat).isEqualTo(BarcodeFormat.UPC_E)
        assertThat(decodedBarcode.text).isEqualTo("01234565")
    }

    @Test
    fun whenUsingEan13_ImageIsGeneratedProperly() {
        val bitmap = BarcodeType.EAN_13.getImageBitmap(104, 128, "9780201379624")
        val binaryBitmap = bitmap.asAndroidBitmap().toBinaryBitmap()

        val decodedBarcode = MultiFormatReader().decode(binaryBitmap)

        assertThat(bitmap).isNotNull()
        assertThat(bitmap.height).isEqualTo(128)
        assertThat(bitmap.width).isEqualTo(104)
        assertThat(decodedBarcode.barcodeFormat).isEqualTo(BarcodeFormat.EAN_13)
        assertThat(decodedBarcode.text).isEqualTo("9780201379624")
    }

    @Test
    fun whenUsingUpcA_ImageIsGeneratedProperly() {
        val bitmap = BarcodeType.UPC_A.getImageBitmap(104, 128, "828999006823")
        val binaryBitmap = bitmap.asAndroidBitmap().toBinaryBitmap()

        val decodedBarcode = MultiFormatReader().decode(binaryBitmap)

        assertThat(bitmap).isNotNull()
        assertThat(bitmap.height).isEqualTo(128)
        assertThat(bitmap.width).isEqualTo(104)
        assertThat(decodedBarcode.barcodeFormat).isEqualTo(BarcodeFormat.UPC_A)
        assertThat(decodedBarcode.text).isEqualTo("828999006823")
    }

    @Test
    fun whenUsingQrCode_ImageIsGeneratedProperly() {
        val bitmap = BarcodeType.QR_CODE.getImageBitmap(200, 242, "https://google.com")
        val binaryBitmap = bitmap.asAndroidBitmap().toBinaryBitmap()

        val decodedBarcode = MultiFormatReader().decode(binaryBitmap)

        assertThat(bitmap).isNotNull()
        assertThat(bitmap.height).isEqualTo(242)
        assertThat(bitmap.width).isEqualTo(200)
        assertThat(decodedBarcode.barcodeFormat).isEqualTo(BarcodeFormat.QR_CODE)
        assertThat(decodedBarcode.text).isEqualTo("https://google.com")
    }

    @Test
    fun whenUsingCode39_ImageIsGeneratedProperly() {
        val bitmap = BarcodeType.CODE_39.getImageBitmap(200, 242, "CODE-39P")
        val binaryBitmap = bitmap.asAndroidBitmap().toBinaryBitmap()

        val decodedBarcode = MultiFormatReader().decode(binaryBitmap)

        assertThat(bitmap).isNotNull()
        assertThat(bitmap.height).isEqualTo(242)
        assertThat(bitmap.width).isEqualTo(200)
        assertThat(decodedBarcode.barcodeFormat).isEqualTo(BarcodeFormat.CODE_39)
        assertThat(decodedBarcode.text).isEqualTo("CODE-39P")
    }

    @Test
    fun whenUsingCode93_ImageIsGeneratedProperly() {
        val bitmap = BarcodeType.CODE_93.getImageBitmap(200, 242, "CODE-39P")
        val binaryBitmap = bitmap.asAndroidBitmap().toBinaryBitmap()

        val decodedBarcode = MultiFormatReader().decode(binaryBitmap)

        assertThat(bitmap).isNotNull()
        assertThat(bitmap.height).isEqualTo(242)
        assertThat(bitmap.width).isEqualTo(200)
        assertThat(decodedBarcode.barcodeFormat).isEqualTo(BarcodeFormat.CODE_93)
        assertThat(decodedBarcode.text).isEqualTo("CODE-39P")
    }

    @Test
    fun whenUsingCode128_ImageIsGeneratedProperly() {
        val bitmap = BarcodeType.CODE_128.getImageBitmap(200, 242, "123456823232")
        val binaryBitmap = bitmap.asAndroidBitmap().toBinaryBitmap()

        val decodedBarcode = MultiFormatReader().decode(binaryBitmap)

        assertThat(bitmap).isNotNull()
        assertThat(bitmap.height).isEqualTo(242)
        assertThat(bitmap.width).isEqualTo(200)
        assertThat(decodedBarcode.barcodeFormat).isEqualTo(BarcodeFormat.CODE_128)
        assertThat(decodedBarcode.text).isEqualTo("123456823232")
    }

    @Test
    fun whenUsingCodeITF_ImageIsGeneratedProperly() {
        val bitmap = BarcodeType.ITF.getImageBitmap(200, 242, "05012345678900")
        val binaryBitmap = bitmap.asAndroidBitmap().toBinaryBitmap()

        val decodedBarcode = MultiFormatReader().decode(binaryBitmap)

        assertThat(bitmap).isNotNull()
        assertThat(bitmap.height).isEqualTo(242)
        assertThat(bitmap.width).isEqualTo(200)
        assertThat(decodedBarcode.barcodeFormat).isEqualTo(BarcodeFormat.ITF)
        assertThat(decodedBarcode.text).isEqualTo("05012345678900")
    }

    @Test
    fun whenUsingPdf417_ImageIsGeneratedProperly() {
        val bitmap = BarcodeType.PDF_417.getImageBitmap(420, 132, "123456823232")
        val binaryBitmap = bitmap.asAndroidBitmap().toBinaryBitmap()

        val decodedBarcode = MultiFormatReader().decode(binaryBitmap)

        assertThat(bitmap).isNotNull()
        assertThat(bitmap.height).isEqualTo(132)
        assertThat(bitmap.width).isEqualTo(420)
        assertThat(decodedBarcode.barcodeFormat).isEqualTo(BarcodeFormat.PDF_417)
        assertThat(decodedBarcode.text).isEqualTo("123456823232")
    }

    @Test
    fun whenUsingCodabar_ImageIsGeneratedProperly() {
        val bitmap = BarcodeType.CODABAR.getImageBitmap(200, 242, "31117013206375")
        val binaryBitmap = bitmap.asAndroidBitmap().toBinaryBitmap()

        val decodedBarcode = MultiFormatReader().decode(binaryBitmap)

        assertThat(bitmap).isNotNull()
        assertThat(bitmap.height).isEqualTo(242)
        assertThat(bitmap.width).isEqualTo(200)
        assertThat(decodedBarcode.barcodeFormat).isEqualTo(BarcodeFormat.CODABAR)
        assertThat(decodedBarcode.text).isEqualTo("31117013206375")
    }

    @Test
    fun whenUsingDatamatrix_ImageIsGeneratedProperly() {
        val bitmap = BarcodeType.DATA_MATRIX.getImageBitmap(200, 242, "123456823232")
        val binaryBitmap = bitmap.asAndroidBitmap().toBinaryBitmap()

        val decodedBarcode = MultiFormatReader().decode(binaryBitmap)

        assertThat(bitmap).isNotNull()
        assertThat(bitmap.height).isEqualTo(242)
        assertThat(bitmap.width).isEqualTo(200)
        assertThat(decodedBarcode.barcodeFormat).isEqualTo(BarcodeFormat.DATA_MATRIX)
        assertThat(decodedBarcode.text).isEqualTo("123456823232")
    }

    @Test
    fun whenUsingAztec_ImageIsGeneratedProperly() {
        val bitmap = BarcodeType.AZTEC.getImageBitmap(200, 242, "123456823232")
        val binaryBitmap = bitmap.asAndroidBitmap().toBinaryBitmap()

        val decodedBarcode = MultiFormatReader().decode(binaryBitmap)

        assertThat(bitmap).isNotNull()
        assertThat(bitmap.height).isEqualTo(242)
        assertThat(bitmap.width).isEqualTo(200)
        assertThat(decodedBarcode.barcodeFormat).isEqualTo(BarcodeFormat.AZTEC)
        assertThat(decodedBarcode.text).isEqualTo("123456823232")
    }

    @Test
    fun whenUsingValidEan8_validIsReturned() =
        BarcodeType.EAN_8.shouldBeValid(
            "9031101",
            "23232426",
            "00000000",
            "99999995",
        )

    @Test
    fun whenUsingInvalidEan8_invalidIsReturned() =
        BarcodeType.EAN_8.shouldBeInvalid(
            "12345678", // bad check digit
            "https://google.com", // not a digit
            "42", // Not enough digits
        )

    @Test
    fun whenUsingValidUpcE_validIsReturned() =
        BarcodeType.UPC_E.shouldBeValid(
            "01234565",
        )

    @Test
    fun whenUsingInvalidUpcE_invalidIsReturned() =
        BarcodeType.UPC_E.shouldBeInvalid(
            "01234567",
        )

    @Test
    fun whenUsingValidEan13_validIsReturned() =
        BarcodeType.EAN_13.shouldBeValid(
            "978020137962",
        )

    @Test
    fun whenUsingInvalidEan13_invalidIsReturned() =
        BarcodeType.EAN_13.shouldBeInvalid(
            "https://google.com",
        )

    @Test
    fun whenUsingValidUpcA_validIsReturned() =
        BarcodeType.UPC_A.shouldBeValid(
            "82899900682",
        )

    @Test
    fun whenUsingInvalidUpcA_invalidIsReturned() =
        BarcodeType.UPC_A.shouldBeInvalid(
            "https://google.com",
        )

    @Test
    fun whenUsingValidQr_validIsReturned() =
        BarcodeType.QR_CODE.shouldBeValid(
            "https://google.com",
            "4242424242424242424",
        )

    @Test
    fun whenUsingInvalidQr_invalidIsReturned() =
        BarcodeType.QR_CODE.shouldBeInvalid(
            "1".repeat(7090),
        )

    @Test
    fun whenUsingValidCode39_validIsReturned() =
        BarcodeType.CODE_39.shouldBeValid(
            "CODE-39P",
        )

    @Test
    fun whenUsingInvalidCode39_invalidIsReturned() =
        BarcodeType.CODE_39.shouldBeInvalid(
            "",
        )

    @Test
    fun whenUsingValidCode93_validIsReturned() =
        BarcodeType.CODE_93.shouldBeValid(
            "CODE-39P",
        )

    @Test
    fun whenUsingInvalidCode93_invalidIsReturned() =
        BarcodeType.CODE_93.shouldBeInvalid(
            "",
        )

    @Test
    fun whenUsingValidCode128_validIsReturned() =
        BarcodeType.CODE_128.shouldBeValid(
            "123456823232",
        )

    @Test
    fun whenUsingInvalidCode128_invalidIsReturned() =
        BarcodeType.CODE_128.shouldBeInvalid(
            "",
        )

    @Test
    fun whenUsingValidItf_validIsReturned() =
        BarcodeType.ITF.shouldBeValid(
            "05012345678900",
        )

    @Test
    fun whenUsingInvalidItf_invalidIsReturned() =
        BarcodeType.ITF.shouldBeInvalid(
            "",
        )

    @Test
    fun whenUsingValidPdf417_validIsReturned() =
        BarcodeType.PDF_417.shouldBeValid(
            "123456823232",
        )

    @Test
    fun whenUsingInvalidPdf417_invalidIsReturned() =
        BarcodeType.PDF_417.shouldBeInvalid(
            "1".repeat(2711),
        )

    @Test
    fun whenUsingValidCodabar_validIsReturned() =
        BarcodeType.CODABAR.shouldBeValid(
            "31117013206375",
        )

    @Test
    fun whenUsingInvalidCodabar_invalidIsReturned() =
        BarcodeType.CODABAR.shouldBeInvalid(
            "",
        )

    @Test
    fun whenUsingValidDatamatrix_validIsReturned() =
        BarcodeType.DATA_MATRIX.shouldBeValid(
            "123456823232",
        )

    @Test
    fun whenUsingInvalidDatamatrix_invalidIsReturned() =
        BarcodeType.DATA_MATRIX.shouldBeInvalid(
            "",
        )

    @Test
    fun whenUsingValidAztec_validIsReturned() =
        BarcodeType.AZTEC.shouldBeValid(
            "123456823232",
        )

    @Test
    fun whenUsingInvalidAztec_invalidIsReturned() =
        BarcodeType.AZTEC.shouldBeInvalid(
            "",
        )

    private fun Bitmap.toBinaryBitmap(): BinaryBitmap {
        val intArray = IntArray(width * height)

        getPixels(intArray, 0, width, 0, 0, width, height)

        val source = RGBLuminanceSource(width, height, intArray)
        return BinaryBitmap(HybridBinarizer(source))
    }

    private fun BarcodeType.shouldBeValid(vararg values: String) {
        values.forEach { toTest ->
            val isValid = isValueValid(toTest)

            Truth.assertWithMessage("%s should be valid for %s", toTest, this.name)
                .that(isValid)
                .isTrue()
        }
    }

    private fun BarcodeType.shouldBeInvalid(vararg values: String) {
        values.forEach { toTest ->
            val isValid = isValueValid(toTest)

            Truth.assertWithMessage("%s should be invalid for %s", toTest, this.name)
                .that(isValid).isFalse()
        }
    }
}