package com.manmadehorrorsstudio.gitscape.ui.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorSystem(
    val primary: Color,
    val onPrimary: Color,

    val secondary: Color,
    val onSecondary: Color,

    val tertiary: Color,
    val onTertiary: Color,

    val background: Color,
    val onBackground: Color,

    val surface: Color,
    val onSurface: Color,

    val surfaceVariant: Color,
    val onSurfaceVariant: Color,

    val surfaceContainerHigh: Color,
    val surfaceContainerLow: Color,

    val scrim: Color
)