# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
