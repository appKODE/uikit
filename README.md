# Uikit

[![Maven Central][mavenbadge-svg]][mavencentral]

A library of commonly used UI components built on [Jetpack Compose](https://developer.android.com/compose).

## Usage

Add the dependency:
```groovy
implementation "ru.kode.uikit:uikit-components:$version"
```
Set up the configuration. It's best to do this as high in the component hierarchy as possible
(app theme, Activity):
```kotlin
val configuration = UiKitConfiguration(
    // TODO
)
CompositionLocalProvider(
    LocalUiKitConfiguration provides configuration
) {
    // TODO
}
```
That completes the setup — you can now use any of the components.

## Components
- Button
- Checkbox
- RadioButton
- ModalBottomSheet
- Slider
- TextField
- TextFieldArea
- PageIndicator
- Shimmer (component and modifier)
- DatePicker

## Component gallery

The `:sample` module is a runnable demo app that hosts a component browser powered by
[Showkase](https://github.com/airbnb/Showkase). It lets you browse every component (grouped by
type, with light/dark, RTL and font-scale toggles) so you can visually track the library as it
changes.

Run it on a device or emulator:
```bash
./gradlew :sample:installDebug
```
Then launch **Uikit Gallery** and tap **Open browser**. Alternatively, open the module in Android
Studio and run the `sample` configuration, or view any preview inline via the IDE Compose preview.

Adding a component to the gallery: drop a parameterless `@Preview @ShowkaseComposable(...)` function
under `sample/src/main/kotlin/ru/kode/uikit/sample/previews/`, wrapping the body in `PreviewTheme { }`
(which supplies a concrete `LocalUikitConfiguration`). Showkase discovers it automatically — no
registration needed.

> The gallery configuration in `PreviewUikitConfiguration.kt` is illustrative and sample-only; it is
> not the library's canonical theme and is not published.

## Publishing

The library is published to Maven Central (Central Portal) via the
[vanniktech/gradle-maven-publish-plugin](https://github.com/vanniktech/gradle-maven-publish-plugin).
The version is defined by `versionName` in `gradle.properties`.

Releases run automatically through GitHub Actions (`.github/workflows/release.yml`) on a git tag push:
```bash
git tag 0.1.3
git push origin 0.1.3
```
The workflow runs `./gradlew publishAndReleaseToMavenCentral`. It requires the following secrets in the
`release` environment: `NEXUS_USERTOKEN_NAME`, `NEXUS_USERTOKEN_PASSWORD`, `SIGNING_KEY_ID`,
`SIGNING_PASSWORD`, `SIGNING_SECRET_KEY_RING_FILE_BASE64`.

To verify the artifact build locally: `./gradlew publishToMavenLocal`.

See [CHANGELOG.md](CHANGELOG.md) for the release history.

## TODO
- Remove the dependency on Material3

[mavenbadge-svg]: https://maven-badges.herokuapp.com/maven-central/ru.kode.uikit/uikit-components/badge.svg
[mavencentral]: https://mvnrepository.com/artifact/ru.kode.uikit/uikit-components
