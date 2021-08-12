Composed Barcodes
=================

Make barcode views with ease in Jetpack Compose.


Gradle users:


```
implementation("com.simonsickle:composed-barcodes:1.0.1")
```

Examples
--------

Displaying barcodes is super easy! Simply call the composable in your layout.

```kotlin
val URL = "https://github.com/simonsickle/ComposedBarcodes"
// Make sure the value is valid for the type of barcode selected. The library will
// just show an infinite spinner in place of a barcode if the data is not valid.
if (BarcodeType.QR_CODE.isValueValid(URL)) {
    Barcode(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        width = 150.dp, // set the width of the code here. This is used to generate the code, so that's why it isn't in the modifier
        height = 150.dp, // set the height of the code here. This is used to generate the code, so that's why it isn't in the modifier
        type = BarcodeType.QR_CODE, // pick the type of barcode you want to render
        value = URL // The textual representation of this code
    )
}

// You must handle invalid data yourself
if (!BarcodeType.CODE_128.isValueValid(URL)) {
    Text("this is not code 128 compatible")
}
```

Supported Barcode Types
-----------------------

We are using ZXing to generate our barcodes and are limited to the types supported by that library. The following are all supported:

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
