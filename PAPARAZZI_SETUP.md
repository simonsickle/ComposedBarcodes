# Paparazzi UI Snapshot Testing Setup

This project uses [Paparazzi](https://github.com/cashapp/paparazzi) for snapshot testing of Compose UI components.

## Overview

Paparazzi enables fast, deterministic screenshot testing without requiring an Android device or emulator. All UI snapshots are generated during unit tests and stored as golden images.

## Configuration

### Versions
- **Android Gradle Plugin**: 8.5.2
- **Kotlin**: 2.0.0
- **Paparazzi**: 1.3.4
- **Compile SDK**: 34

### Build Configuration

Paparazzi is configured in both `app/build.gradle` and `composed-barcodes/build.gradle`:

```gradle
plugins {
    id 'app.cash.paparazzi'
}

dependencies {
    testImplementation(libs.junit)
}
```

## Snapshot Tests

### Library Module (`composed-barcodes`)

**Location**: `composed-barcodes/src/test/kotlin/com/simonsickle/compose/barcodes/`

#### Test Files:
- `BarcodeSnapshotTest.kt` - Tests all Barcode component preview variations
- `SimplePreviewTest.kt` - Basic verification test

#### Preview Composables:
Created in `BarcodePreviews.kt`, covering:
- QR Code (default, small, high-resolution, no-progress variations)
- Code 128, Code 39, EAN-13, UPC-A
- Data Matrix, Aztec, PDF417

### App Module (`app`)

**Location**: `app/src/test/java/com/simonsickle/composedbarcodes/examples/`

#### Test Files:
- `BarcodePreviewSnapshotTest.kt` - Tests all barcode example screens

Covers all 13 barcode types:
QR, Aztec, Code39, Code93, Code128, Codabar, DataMatrix, EAN8, EAN13, ITF, PDF417, UPC-A, UPC-E

## Running Tests

### Record Golden Images
```bash
# Record all snapshots
./gradlew recordPaparazziDebug

# Record for specific module
./gradlew composed-barcodes:recordPaparazziDebug
./gradlew app:recordPaparazziDebug
```

### Verify Snapshots
```bash
# Verify all snapshots match golden images
./gradlew verifyPaparazziDebug

# Verify for specific module
./gradlew composed-barcodes:verifyPaparazziDebug
./gradlew app:verifyPaparazziDebug
```

### View Reports
After running tests, view the generated report at:
- Library: `composed-barcodes/build/reports/paparazzi/debug/index.html`
- App: `app/build/reports/paparazzi/debug/index.html`

## CI Integration

The GitHub Actions workflow (`.github/workflows/unit-tests.yml`) runs Paparazzi tests automatically on push and pull requests:

```yaml
- name: Run Paparazzi snapshot tests - Library
  run: ./gradlew composed-barcodes:verifyPaparazziDebug --stacktrace

- name: Run Paparazzi snapshot tests - App
  run: ./gradlew app:verifyPaparazziDebug --stacktrace
```

## Implementation Details

### Synchronous Barcode for Testing

To ensure snapshots capture actual barcodes instead of loading states, the library provides a `SynchronousBarcode` composable specifically for preview and testing purposes. This version generates barcodes synchronously during composition, making it ideal for Paparazzi snapshot tests.

- **Production use**: Use the async `Barcode` composable with loading indicator support
- **Testing/Previews**: Use `SynchronousBarcode` for reliable snapshot testing

All preview composables in `BarcodePreviews.kt` use `SynchronousBarcode` to ensure accurate snapshot rendering.

## Known Limitations

1. **App Module**: There's a known Gradle dependency version mismatch in the app module (requires compileSdk 35 and AGP 8.6.0+, currently using 34 and 8.5.2). This doesn't affect the library module tests.

## Troubleshooting

### Tests Failing After Dependency Updates
Run `./gradlew --stop` to stop all Gradle daemons, then re-run tests.

### Snapshot Mismatches
If legitimate UI changes were made, record new golden images:
```bash
./gradlew recordPaparazziDebug
```

### Build Failures
Ensure you're using the correct SDK version (34) and the specified plugin versions.

## Best Practices

1. **Always record golden images** after intentional UI changes
2. **Review snapshot diffs** carefully before accepting changes
3. **Keep snapshots small** - test individual components rather than entire screens when possible
4. **Use descriptive test names** to make snapshot files easy to identify
