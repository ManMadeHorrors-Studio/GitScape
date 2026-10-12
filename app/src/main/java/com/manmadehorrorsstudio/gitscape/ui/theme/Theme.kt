package com.manmadehorrorsstudio.gitscape.ui.theme

import android.content.Context
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.manmadehorrorsstudio.gitscape.DYNAMIC_COLOR
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.models.ColorSchemeType
import com.manmadehorrorsstudio.gitscape.ui.models.ColorStyle
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
import javax.inject.Inject



@Composable
fun GitScapeTheme(
    isDarkTheme: Boolean,
    colorStyle: ColorStyle,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorSchemeType =
        if(isDarkTheme)
            colorStyle.dark
        else
            colorStyle.light

    MaterialTheme(
        colorScheme = getGitScapeColorScheme(colorSchemeType, context),
        typography = GitScapeTypography,
        content = content
    )
}

private fun getGitScapeColorScheme(colorSchemeType: ColorSchemeType, context: Context) : ColorScheme {
    val idDynamic = DYNAMIC_COLOR && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val baseColorScheme = when {
        idDynamic && colorSchemeType.darkScheme -> dynamicDarkColorScheme(context)
        idDynamic && !colorSchemeType.darkScheme -> dynamicLightColorScheme(context)
        colorSchemeType.darkScheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    val colorScheme = baseColorScheme.copy(
        primary = colorSchemeType.colorSystem.primary,
        onPrimary = colorSchemeType.colorSystem.onPrimary,

        secondary = colorSchemeType.colorSystem.secondary,
        onSecondary = colorSchemeType.colorSystem.onSecondary,

        tertiary = colorSchemeType.colorSystem.tertiary,
        onTertiary = colorSchemeType.colorSystem.onTertiary,

        background = colorSchemeType.colorSystem.background,
        onBackground = colorSchemeType.colorSystem.onBackground,

        surface = colorSchemeType.colorSystem.surface,
        onSurface = colorSchemeType.colorSystem.onSurface,

        surfaceVariant = colorSchemeType.colorSystem.surfaceVariant,
        onSurfaceVariant = colorSchemeType.colorSystem.onSurfaceVariant,

        scrim = colorSchemeType.colorSystem.scrim,

        surfaceContainerHigh = colorSchemeType.colorSystem.surfaceContainerHigh,
        surfaceContainerLow = colorSchemeType.colorSystem.surfaceContainerLow
    )
    return colorScheme
}
