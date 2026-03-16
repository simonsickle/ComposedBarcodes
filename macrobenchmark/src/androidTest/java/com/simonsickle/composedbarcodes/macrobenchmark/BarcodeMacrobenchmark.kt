package com.simonsickle.composedbarcodes.macrobenchmark

import androidx.benchmark.macro.FrameOverrunMetric
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkRule
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Arrays
import java.util.Collections

@RunWith(AndroidJUnit4::class)
@LargeTest
class BarcodeMacrobenchmark {
    @Rule
    var benchmarkRule: MacrobenchmarkRule = MacrobenchmarkRule()

    @Test
    fun startupCold() {
        benchmarkRule.measureRepeated(
            com.simonsickle.composedbarcodes.macrobenchmark.BarcodeMacrobenchmark.Companion.TARGET_PACKAGE,
            Collections.singletonList(StartupTimingMetric()),
            Partial(),
            StartupMode.COLD,
            12,
            { scope ->
                scope.pressHome()
                Unit.INSTANCE
            },
            { scope ->
                scope.startActivityAndWait()
                Unit.INSTANCE
            }
        )
    }

    @Test
    fun openQrCode() {
        benchmarkRule.measureRepeated(
            com.simonsickle.composedbarcodes.macrobenchmark.BarcodeMacrobenchmark.Companion.TARGET_PACKAGE,
            Arrays.asList(FrameTimingMetric(), FrameOverrunMetric()),
            Partial(),
            StartupMode.WARM,
            20,
            { scope ->
                scope.startActivityAndWait()
                waitForText("QR_CODE")
                Unit.INSTANCE
            },
            { scope ->
                tapText("QR_CODE")
                waitForText("QR Code")
                scope.pressHome()
                Unit.INSTANCE
            }
        )
    }

    private fun tapText(text: String?) {
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val node: UiObject2 = device.findObject(By.text(text))
        Assert.assertNotNull("Could not find text: " + text, node)
        node.click()
        device.waitForIdle()
    }

    private fun waitForText(text: String?) {
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val found: Boolean = device.wait(
            Until.hasObject(By.text(text)),
            com.simonsickle.composedbarcodes.macrobenchmark.BarcodeMacrobenchmark.Companion.UI_TIMEOUT_MS
        )
        Assert.assertTrue("Timed out waiting for text: " + text, found)
    }

    companion object {
        private val TARGET_PACKAGE = "com.simonsickle.composedbarcodes"
        private const val UI_TIMEOUT_MS = 5000L
    }
}
