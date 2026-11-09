package ru.kode.uikit.core.internal

/** Shared between the uikit backends; not a stable public API. */
@RequiresOptIn(
  message = "Internal uikit API shared between backends. It may change without notice.",
  level = RequiresOptIn.Level.WARNING,
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class InternalUikitApi
