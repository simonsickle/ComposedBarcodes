package com.simonsickle.compose.barcodes

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.qrcode.encoder.Encoder
import com.google.zxing.qrcode.encoder.QRCode
import com.simonsickle.compose.barcodes.qr.FINDER_PATTERN_ROW_COUNT
import com.simonsickle.compose.barcodes.qr.drawFinders

enum class QrCodeAppearance {
    Standard,
    Rounded,
    Connected
}

@Composable
fun QrCode(
    contents: String,
    modifier: Modifier = Modifier,
    qrCodeAppearance: QrCodeAppearance = QrCodeAppearance.Rounded,
    margins: Float = 16f,
    dataBitsColor: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    backgroundColor: Color = if (isSystemInDarkTheme()) Color.Black else Color.White
) {
    Canvas(
        modifier = modifier
            .defaultMinSize(96.dp, 96.dp)
    ) {
        require(size.height == size.width) { "Canvas must be square" }

        // Generate QR code
        val qrCode = createQrCode(
            contents = contents,
            marginPx = margins
        )

        val bytes = qrCode.matrix

        val rowHeight = (size.height - margins * 2f) / bytes.height
        val columnWidth = (size.width - margins * 2f) / bytes.width

        val backgroundPaint = Paint().apply {
            strokeCap = StrokeCap.Square
            color = backgroundColor
        }

        val foregroundPaint = Paint().apply {
            strokeCap = StrokeCap.Square
            color = dataBitsColor
        }

        drawIntoCanvas {
            it.drawRect(
                rect = Rect(
                    offset = Offset(0f, 0f),
                    size = size
                ),
                paint = backgroundPaint
            )

            drawFinders(
                qrCodeAppearance = qrCodeAppearance,
                marginSize = margins,
                sideLength = size.width,
                finderPatternSize = Size(
                    width = columnWidth * FINDER_PATTERN_ROW_COUNT,
                    height = rowHeight * FINDER_PATTERN_ROW_COUNT
                ),
                foregroundPaint = foregroundPaint
            )

            // TODO data bits

            // TODO logo center, if set
        }
    }
}

@Preview
@Composable
private fun PreviewQrCode() = QrCode(contents = "https://google.com")

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun NightModeQrCode() = PreviewQrCode()

internal fun createQrCode(
    contents: String,
    marginPx: Float,
    errorCorrectionLevel: ErrorCorrectionLevel = ErrorCorrectionLevel.Q
): QRCode {
    require(contents.isNotEmpty())

    return Encoder.encode(
        contents, errorCorrectionLevel, mapOf(
            EncodeHintType.MARGIN to marginPx,
            EncodeHintType.ERROR_CORRECTION to errorCorrectionLevel
        )
    )
}
