package com.manmadehorrorsstudio.gitscape.ui.models

import com.manmadehorrorsstudio.gitscape.ui.theme.*

enum class ColorSchemeType(val colorSystem: ColorSystem, val darkScheme: Boolean) {
    AZURE_DARK(
        colorSystem = ColorSystem(
            primary = StrongAzure,
            onPrimary = GrayBackground,

            secondary = LightAzure,
            onSecondary = CardBackground,

            tertiary = DisabledAzure,
            onTertiary = CardBackground,

            background = GrayBackground,
            onBackground = StrongAzure,

            surface = GrayBackground,
            onSurface = StrongAzure,

            surfaceVariant = CardBackground,
            onSurfaceVariant = LightAzure,

            scrim = DarkAzure,

            surfaceContainerHigh = DarkAzure,
            surfaceContainerLow = DisabledAzure
        ),
        darkScheme = true
    ),
    AZURE_LIGHT(
        colorSystem = ColorSystem(
            primary = StrongAzure,
            onPrimary = GrayBackground,

            secondary = LightAzure,
            onSecondary = CardBackground,

            tertiary = DisabledAzure,
            onTertiary = CardBackground,

            background = GrayBackground,
            onBackground = StrongAzure,

            surface = GrayBackground,
            onSurface = StrongAzure,

            surfaceVariant = CardBackground,
            onSurfaceVariant = LightAzure,

            scrim = DarkAzure,

            surfaceContainerHigh = DarkAzure,
            surfaceContainerLow = DisabledAzure
        ),
        darkScheme = false
    )
}