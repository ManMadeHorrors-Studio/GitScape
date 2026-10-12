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
    ),

    GREEN_DARK(
        colorSystem = ColorSystem(
        primary = StrongGreen_dark,
        onPrimary = GrayBackground_dark,

        secondary = LightGreen_dark,
        onSecondary = CardBackground_dark,

        tertiary = DisabledGreen_dark,
        onTertiary = CardBackground_dark,

        background = GrayBackground_dark,
        onBackground = StrongGreen_dark,

        surface = GrayBackground_dark,
        onSurface = StrongGreen_dark,

        surfaceVariant = CardBackground_dark,
        onSurfaceVariant = LightGreen_dark,

        scrim = DarkGreen,

        surfaceContainerHigh = CardBackground_dark,
        surfaceContainerLow = DisabledGreen_dark
    ),
        darkScheme = true
    ),

    GREEN_LIGHT(
        colorSystem = ColorSystem(
        primary = StrongGreen_light,
        onPrimary = GrayBackground_light,

        secondary = LightGreen_light,
        onSecondary = CardBackground_light,

        tertiary = DisabledGreen_light,
        onTertiary = CardBackground_light,

        background = GrayBackground_light,
        onBackground = StrongGreen_light,

        surface = GrayBackground_light,
        onSurface = StrongGreen_light,

        surfaceVariant = CardBackground_light,
        onSurfaceVariant = LightGreen_light,

        scrim = DarkGreen,

        surfaceContainerHigh = CardBackground_light,
        surfaceContainerLow = DisabledGreen_light
    ),
        darkScheme = false
    ),

    RED_DARK(
        colorSystem = ColorSystem(
            primary = StrongRed_dark,
            onPrimary = GrayBackground_dark,

            secondary = LightRed_dark,
            onSecondary = CardBackground_dark,

            tertiary = DisabledRed_dark,
            onTertiary = CardBackground_dark,

            background = GrayBackground_dark,
            onBackground = StrongRed_dark,

            surface = GrayBackground_dark,
            onSurface = StrongRed_dark,

            surfaceVariant = CardBackground_dark,
            onSurfaceVariant = LightRed_dark,

            scrim = DarkRed,

            surfaceContainerHigh = CardBackground_dark,
            surfaceContainerLow = DisabledRed_dark
        ),
        darkScheme = true
    ),

    RED_LIGHT(
        colorSystem = ColorSystem(
            primary = StrongRed_light,
            onPrimary = GrayBackground_light,

            secondary = LightRed_light,
            onSecondary = CardBackground_light,

            tertiary = DisabledRed_light,
            onTertiary = CardBackground_light,

            background = GrayBackground_light,
            onBackground = StrongRed_light,

            surface = GrayBackground_light,
            onSurface = StrongRed_light,

            surfaceVariant = CardBackground_light,
            onSurfaceVariant = LightRed_light,

            scrim = DarkRed,

            surfaceContainerHigh = CardBackground_light,
            surfaceContainerLow = DisabledRed_light
        ),
        darkScheme = false
    ),

    PINK_DARK(
        colorSystem = ColorSystem(
            primary = StrongPink_dark,
            onPrimary = GrayBackground_dark,

            secondary = LightPink_dark,
            onSecondary = CardBackground_dark,

            tertiary = DisabledPink_dark,
            onTertiary = CardBackground_dark,

            background = GrayBackground_dark,
            onBackground = StrongPink_dark,

            surface = GrayBackground_dark,
            onSurface = StrongPink_dark,

            surfaceVariant = CardBackground_dark,
            onSurfaceVariant = LightPink_dark,

            scrim = DarkPink,

            surfaceContainerHigh = CardBackground_dark,
            surfaceContainerLow = DisabledPink_dark
        ),
        darkScheme = true
    ),

    PINK_LIGHT(
        colorSystem = ColorSystem(
            primary = StrongPink_light,
            onPrimary = GrayBackground_light,

            secondary = LightPink_light,
            onSecondary = CardBackground_light,

            tertiary = DisabledPink_light,
            onTertiary = CardBackground_light,

            background = GrayBackground_light,
            onBackground = StrongPink_light,

            surface = GrayBackground_light,
            onSurface = StrongPink_light,

            surfaceVariant = CardBackground_light,
            onSurfaceVariant = LightPink_light,

            scrim = DarkPink,

            surfaceContainerHigh = CardBackground_light,
            surfaceContainerLow = DisabledPink_light
        ),
        darkScheme = false
    ),

    YELLOW_DARK(
        colorSystem = ColorSystem(
            primary = StrongYellow_dark,
            onPrimary = GrayBackground_dark,

            secondary = LightYellow_dark,
            onSecondary = CardBackground_dark,

            tertiary = DisabledYellow_dark,
            onTertiary = CardBackground_dark,

            background = GrayBackground_dark,
            onBackground = StrongYellow_dark,

            surface = GrayBackground_dark,
            onSurface = StrongYellow_dark,

            surfaceVariant = CardBackground_dark,
            onSurfaceVariant = LightYellow_dark,

            scrim = DarkYellow,

            surfaceContainerHigh = CardBackground_dark,
            surfaceContainerLow = DisabledYellow_dark
        ),
        darkScheme = true
    ),

    YELLOW_LIGHT(
        colorSystem = ColorSystem(
            primary = StrongYellow_light,
            onPrimary = GrayBackground_light,

            secondary = LightYellow_light,
            onSecondary = CardBackground_light,

            tertiary = DisabledYellow_light,
            onTertiary = CardBackground_light,

            background = GrayBackground_light,
            onBackground = StrongYellow_light,

            surface = GrayBackground_light,
            onSurface = StrongYellow_light,

            surfaceVariant = CardBackground_light,
            onSurfaceVariant = LightYellow_light,

            scrim = DarkYellow,

            surfaceContainerHigh = CardBackground_light,
            surfaceContainerLow = DisabledYellow_light
        ),
        darkScheme = false
    ),

}