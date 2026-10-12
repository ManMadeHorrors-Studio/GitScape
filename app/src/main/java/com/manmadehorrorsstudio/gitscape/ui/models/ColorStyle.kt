package com.manmadehorrorsstudio.gitscape.ui.models

import androidx.compose.ui.graphics.Color
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.ui.models.ColorSchemeType.*

enum class ColorStyle (
    val description: Int,
    val representiveColor: Color,
    val dark: ColorSchemeType,
    val light: ColorSchemeType
) {
    AZURE_STYLE(R.string.SETTINGSVIEW_COLORSTYLE_AZURE, AZURE_DARK.colorSystem.primary, AZURE_DARK, AZURE_LIGHT),
    GREEN_STYLE(R.string.SETTINGSVIEW_COLORSTYLE_GREEN, GREEN_DARK.colorSystem.primary, GREEN_DARK, GREEN_LIGHT),
    RED_STYLE(R.string.SETTINGSVIEW_COLORSTYLE_RED, RED_DARK.colorSystem.primary, RED_DARK, RED_LIGHT),
    PINK_STYLE(R.string.SETTINGSVIEW_COLORSTYLE_PINK, PINK_DARK.colorSystem.primary, PINK_DARK, PINK_LIGHT),
    YELLOW_STYLE(R.string.SETTINGSVIEW_COLORSTYLE_YELLOW, YELLOW_DARK.colorSystem.primary, YELLOW_DARK, YELLOW_LIGHT)
}