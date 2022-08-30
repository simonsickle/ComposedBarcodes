package com.simonsickle.compose.barcodes

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType

internal fun path(lambda: Path.() -> Unit): Path = Path().apply {
    fillType = PathFillType.EvenOdd
    lambda(this)
}