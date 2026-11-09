# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 0.4.0

### Added
- Kotlin Multiplatform: all three artifacts now publish for Android, iOS (`iosArm64`, `iosSimulatorArm64`),
  JVM desktop, `js` and `wasmJs`. Android consumers keep the same coordinates; Gradle module metadata picks the
  Android variant.
- The Material3 backend uses androidx Material3 1.4.0 on Android (unchanged) and JetBrains Compose
  Multiplatform Material3 on the other targets. The foundation backend is pure Compose Foundation everywhere.

### Changed (breaking)
- Dates are `kotlinx.datetime.LocalDate` instead of `java.time.LocalDate`: `rememberDatePickerState`,
  `DatePickerState.selectedDate`, `onDateConfirm`, the `headline` slot and `UikitDatePickerDefaults.Headline`.
- Android resource ids in the date picker configuration are replaced by composable lambdas:
  `TextFieldDatePickerConfiguration.leadingIcon/trailingIcon/errorIcon`, `InlineDatePickerConfiguration.icon`
  (`@Composable () -> Painter`) and `ModalDatePickerConfiguration.title/confirmButtonText/cancelButtonText`
  (`@Composable () -> String`). Migration:
  ```kotlin
  // before
  TextFieldDatePickerConfiguration(leadingIconRes = R.drawable.ic_calendar, /* … */)
  ModalDatePickerConfiguration(titleRes = R.string.pick_date, /* … */)
  rememberDatePickerState(initialSelectedDate = java.time.LocalDate.of(2026, 1, 15))
  // after
  TextFieldDatePickerConfiguration(leadingIcon = { painterResource(R.drawable.ic_calendar) }, /* … */)
  ModalDatePickerConfiguration(title = { stringResource(R.string.pick_date) }, /* … */)
  rememberDatePickerState(initialSelectedDate = kotlinx.datetime.LocalDate(2026, 1, 15))
  ```

### Notes
- The libraries no longer enable core library desugaring themselves. kotlinx-datetime uses `java.time` on
  Android, so apps with `minSdk < 26` need `coreLibraryDesugaring` enabled.
- No `iosX64` (Intel simulator) target: Compose Multiplatform 1.11 no longer publishes it.

## 0.3.0

### Added
- The library is split into three artifacts. Pick **one** backend; both depend on `uikit-core` and expose the
  same API in `ru.kode.uikit.components`, so switching is a one-line dependency change:
  - `uikit-core`: configuration (`UikitConfiguration`, all `*Configuration` classes), shared state types and the
    Material3-free components (`TextField`, `TextFieldArea`, `PageIndicator`, shimmer).
  - `uikit-components`: the Material3 backend (same artifact id as before, so existing apps just bump the version).
  - `uikit-components-foundation`: a Compose Foundation backend with no Material3 or Accompanist dependency.
  Adding both backends fails the build with duplicate classes.
- Slots for overriding a component's visual part on either backend:
  `Checkbox(box = { state: CheckboxSlotState -> … })`, `RadioButton(indicator = { state: RadioButtonSlotState -> … })`,
  `Button(loadingIndicator = { … })`, `ModalBottomSheet(dragHandle = { … } or null)`,
  `TextFieldDatePicker(title = { configuration -> … }, headline = { date -> … })`.
  Defaults live in `UikitCheckboxDefaults`, `UikitRadioButtonDefaults`, `UikitButtonDefaults`,
  `UikitBottomSheetDefaults` and `UikitDatePickerDefaults.Title` / `.Headline`.
- Both backends render pixel-identically; CI verifies each against the same screenshot goldens.

### Changed (breaking)
- `Slider`: the `thumb`/`track`/`startThumb`/`endThumb` slot lambdas now receive the library's own
  `UikitSliderState` / `UikitRangeSliderState` instead of Material3 `SliderState` / `RangeSliderState`.
- `DatePicker`: `rememberDatePickerState(selectableDates = …)` and `DatePickerState.selectableDates` now use
  the library's own `SelectableDates` interface (with `UikitDatePickerDefaults.AllDates` / `.YearRange`)
  instead of Material3's `SelectableDates` / `DatePickerDefaults`.

### Deprecated
- `ModalBottomSheet(showHandle = …)`: use `dragHandle` (`null` hides the handle).

### Known differences
- The foundation `ModalBottomSheet` does not provide Material3's `LocalContentColor`, so Material3 `Text` without
  an explicit color renders black inside it.
- The foundation sliders size the thumb gap from `SliderConfiguration`, not from a custom `thumb` slot, and draw
  no step ticks (the Material3 backend draws none with the current configuration either).

## 0.2.0

### Added
- GitHub Actions CI/CD: `release.yml` (publishes to Maven Central on a git tag push),
  `pre-merge.yaml` (Spotless check + release assembly on PRs and `master`), and
  `gradle-wrapper-validation.yml`.
- `CHANGELOG.md`.

### Changed
- Migrated publishing from the built-in `maven-publish`/`signing` plugins (Sonatype OSSRH `s01`, now
  shut down) to the [vanniktech maven-publish plugin](https://github.com/vanniktech/gradle-maven-publish-plugin)
  targeting the Maven Central Portal (`publishAndReleaseToMavenCentral`).
- Updated dependencies: Kotlin `2.0.0` → `2.4.0`, Android Gradle Plugin `8.3.2` → `9.2.1`,
  Gradle `8.9` → `9.6.1`, Compose `1.7.4` → `1.11.4`, Material3 `1.3.0` → `1.4.0`,
  Coroutines `1.9.0` → `1.11.0`, Accompanist `0.32.0` → `0.36.0`, Spotless `6.25.0` → `8.8.0`,
  ktlint `0.49.1` → `1.8.0`, desugar_jdk_libs `2.1.3` → `2.1.5`.
- Bumped `compileSdk` from `34` to `36`.
- Migrated the build to AGP 9 (Kotlin support is now built in — the `org.jetbrains.kotlin.android`
  plugin is no longer applied).
- Rewrote the README in English and documented the publishing/release flow.

### Removed
- Orphaned version-catalog entries: the standalone `compose-compiler` library (superseded by the
  Kotlin Compose compiler plugin) and the unused `ktlint` library.

## [0.1.2] - 2024-11-29

### Added
- `DatePicker` component.

### Changed
- Upgraded Gradle to 8.9, set the Java version to 17, and enabled Java 8+ API desugaring.
- Switched the configuration `CompositionLocal` provider to a local provider.

## [0.1.1] - 2024-11-14

### Changed
- Lowered `minSdk` to 21.

## [0.1.0] - 2024-11-11

### Added
- Initial release with the core components: `Button`, `Checkbox`, `RadioButton`, `ModalBottomSheet`,
  `Slider`, `TextField`, `TextFieldArea`, `PageIndicator`, and `Shimmer`.
- Library publishing to Maven Central.

[Unreleased]: https://github.com/appKODE/uikit/compare/0.1.2...HEAD
[0.1.2]: https://github.com/appKODE/uikit/compare/0.1.1...0.1.2
[0.1.1]: https://github.com/appKODE/uikit/compare/721324b...0.1.1
[0.1.0]: https://github.com/appKODE/uikit/commit/721324b
