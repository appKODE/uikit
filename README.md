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
