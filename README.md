Composed Barcodes
=================

Make barcode views with ease in Jetpack Compose.

Gradle users:

```
implementation("com.simonsickle:composed-barcodes:1.3.0")
```

Examples
--------

Displaying barcodes is super easy! Simply call the composable in your layout.

```kotlin
val URL = "https://github.com/simonsickle/ComposedBarcodes"
// Make sure the value is valid for the type of barcode selected.
// Invalid data will not render a barcode.
if (BarcodeType.QR_CODE.isValueValid(URL)) {
    Barcode(
        modifier = Modifier.align(Alignment.CenterHorizontally)
            .width(150.dp)
            .height(150.dp),
        resolutionFactor = 10, // Optionally, increase the resolution of the generated image
        type = BarcodeType.QR_CODE, // pick the type of barcode you want to render
        value = URL, // The textual representation of this code
        // Optional ZXing hints such as custom character encoding.
        encodeHints = BarcodeEncodeHints.of(
            EncodeHintType.CHARACTER_SET to Charsets.UTF_8.name()
        )
    )
}

// You must handle invalid data yourself
if (!BarcodeType.CODE_128.isValueValid(URL)) {
    Text("this is not code 128 compatible")
}
```

Supported Barcode Types
-----------------------

We are using ZXing to generate our barcodes and are limited to the types supported by that library.
The following are all supported:

```
EAN_8
UPC_E
EAN_13
UPC_A
QR_CODE
CODE_39
CODE_93
CODE_128
ITF
PDF_417
CODABAR
DATA_MATRIX
AZTEC
```

Benchmarking and Perf Verification
----------------------------------

The library now includes instrumentation benchmark + verification tests in
`composed-barcodes/src/androidTest/kotlin/com/simonsickle/compose/barcodes`.

Run benchmark suites on a connected device:

```bash
./gradlew :composed-barcodes:connectedDebugAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.simonsickle.compose.barcodes.BarcodePipelineBenchmark
```

Run regression/perf verification assertions:

```bash
./gradlew :composed-barcodes:connectedDebugAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.simonsickle.compose.barcodes.BarcodePerformanceVerificationTest
```

Run macrobenchmarks (startup + Compose frame timing in the sample app):

```bash
./gradlew :macrobenchmark:connectedBenchmarkAndroidTest
```

Capture golden macrobenchmark timings after a trusted run:

```bash
./gradlew :macrobenchmark:captureMacrobenchmarkGolden
```

Verify latest macrobenchmark run against the saved golden timings:

```bash
./gradlew :macrobenchmark:verifyMacrobenchmarkGolden
```
