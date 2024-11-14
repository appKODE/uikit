# Uikit

[![Maven Central][mavenbadge-svg]][mavencentral]

Библиотека, содержащая часто используемые ui-компоненты на [Jetpack Compose](https://developer.android.com/compose).

## Использование

Подключение зависимости.
```groovy
implementation "ru.kode.uikit:uikit-components:$version"
```
Настройка конфигурации. Целесообразно сделать это как можно выше в иерархии компонентов (тема приложения, Activity).
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
На этом настройка закончена. Можно использовать любые компоненты.

## Компоненты
- Button
- Checkbox
- RadioButton
- ModalBottomSheet
- Slider
- TextField
- TextFieldArea
- PageIndicator
- Shimmer(компонент и модификатор)

## TODO
- Добавить DatePicker
- Избавиться от зависимости на Material3

[mavenbadge-svg]: https://maven-badges.herokuapp.com/maven-central/ru.kode.uikit/uikit-components/badge.svg
[mavencentral]: https://mvnrepository.com/artifact/ru.kode.uikit/uikit-components