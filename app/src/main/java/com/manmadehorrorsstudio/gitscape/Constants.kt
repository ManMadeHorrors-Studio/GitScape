package com.manmadehorrorsstudio.gitscape

import androidx.annotation.DrawableRes
import java.util.Locale

//URLs

const val GITHUB_BASE_PATH = "api.github.com"
const val GITHUB_GET_REPOS = "user/repos"
const val MAX_REPOS_NUMBER = 50
const val GITHUB_GET_USER = "user"

//DB

const val GITSCAPE_DATABASE = "gitscape_database"
const val SETTINGS_TABLE_NAME = "settings_table_name"


//Language

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