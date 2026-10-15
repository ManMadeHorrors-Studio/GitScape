package com.manmadehorrorsstudio.gitscape.ui.models

import com.manmadehorrorsstudio.gitscape.R

enum class ThemeStyle (
    val description: Int,
    val icon: Int
) {
    DEFAULT(R.string.SETTINGSVIEW_THEME_DEFAULT, R.drawable.ic_default_theme),
    LIGHT(R.string.SETTINGSVIEW_THEME_LIGHT, R.drawable.ic_light_theme),
    DARK(R.string.SETTINGSVIEW_THEME_DARK, R.drawable.ic_dark_theme)
}