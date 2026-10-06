package com.manmadehorrorsstudio.gitscape.ui.models

import androidx.annotation.DrawableRes
import com.manmadehorrorsstudio.gitscape.R
import java.util.Locale

enum class AppLanguage(
    val locale: Locale,
    val description: Int,
    @DrawableRes
    val flag:Int
) {
    EN(
        locale = Locale.ENGLISH,
        description = R.string.SETTINGSVIEW_SETTING_LANGUAGE_ENGLISH,
        flag = R.drawable.language_uk_logo
    ),
    IT(
        locale = Locale.ITALIAN,
        description = R.string.SETTINGSVIEW_SETTING_LANGUAGE_ITALIAN,
        flag = R.drawable.language_ita_logo
    );

    companion object {
        fun fromLanguageCode(code: String?): AppLanguage {
            val codeOrLocaleDefault = code ?: Locale.getDefault().language
            return AppLanguage.entries.find { entry ->
                entry.locale.language.equals(codeOrLocaleDefault, ignoreCase = true)
            } ?: EN
        }
    }
}