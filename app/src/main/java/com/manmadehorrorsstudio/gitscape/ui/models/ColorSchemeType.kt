package com.manmadehorrorsstudio.gitscape.ui.models

import com.manmadehorrorsstudio.gitscape.ui.theme.*

enum class ColorSchemeType(val colorSystem: ColorSystem, val darkScheme: Boolean) {
    AZURE_DARK(
        colorSystem = ColorSystem(
            primary = StrongAzure_dark,
            onPrimary = GrayBackground_dark,

            secondary = LightAzure_dark,
            onSecondary = CardBackground_dark,

            tertiary = DisabledAzure_dark,
            onTertiary = CardBackground_dark,

            background = GrayBackground_dark,
            onBackground = StrongAzure_dark,

            surface = GrayBackground_dark,
            onSurface = StrongAzure_dark,

            surfaceVariant = CardBackground_dark,
            onSurfaceVariant = LightAzure_dark,

            scrim = DarkAzure,

            surfaceContainerHigh = CardBackground_dark,
            surfaceContainerLow = DisabledAzure_dark
        ),
        darkScheme = true
    ),
    AZURE_LIGHT(
        colorSystem = ColorSystem(
            primary = StrongAzure_light,
            onPrimary = GrayBackground_light,

            secondary = LightAzure_light,
            onSecondary = CardBackground_light,

            tertiary = DisabledAzure_light,
            onTertiary = CardBackground_light,

            background = GrayBackground_light,
            onBackground = StrongAzure_light,

            surface = GrayBackground_light,
            onSurface = StrongAzure_light,

            surfaceVariant = CardBackground_light,
            onSurfaceVariant = LightAzure_light,

            scrim = DarkAzure,

            surfaceContainerHigh = CardBackground_light,
            surfaceContainerLow = DisabledAzure_light
        ),
        darkScheme = false
    )
}