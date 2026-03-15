package com.simonsickle.compose.barcodes

import androidx.compose.runtime.Immutable
import com.google.zxing.EncodeHintType

/**
 * Immutable wrapper around ZXing encode hints to keep Compose APIs stable.
 */
@Immutable
class BarcodeEncodeHints private constructor(
    internal val values: Map<EncodeHintType, Any>
) {
    companion object {
        val None = BarcodeEncodeHints(emptyMap())

        fun from(hints: Map<EncodeHintType, *>?): BarcodeEncodeHints {
            if (hints.isNullOrEmpty()) return None

            val normalizedHints = mutableMapOf<EncodeHintType, Any>()
            hints.forEach { (type, value) ->
                if (value != null) {
                    normalizedHints[type] = value
                }
            }

            return if (normalizedHints.isEmpty()) None else BarcodeEncodeHints(normalizedHints.toMap())
        }

        fun of(vararg hints: Pair<EncodeHintType, Any>): BarcodeEncodeHints {
            if (hints.isEmpty()) return None
            return BarcodeEncodeHints(mapOf(*hints))
        }
    }

    override fun equals(other: Any?): Boolean =
        other is BarcodeEncodeHints && values == other.values

    override fun hashCode(): Int = values.hashCode()
}
