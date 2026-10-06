package com.manmadehorrorsstudio.gitscape

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//URLs

const val GITHUB_BASE_PATH = "api.github.com"
const val GITHUB_GET_REPOS = "user/repos"
const val MAX_REPOS_NUMBER = 50
const val GITHUB_GET_USER = "user"

//DB

const val GITSCAPE_DATABASE = "gitscape_database"
const val SETTINGS_TABLE_NAME = "settings_table_name"

//Navigation

const val MAIN_NAVIGATION_GROUP = "MAIN_NAVIGATION_GROUP"

sealed class NavigationScreen(val route: String) {
    data object RepoList : NavigationScreen("repolist")
    data object Settings : NavigationScreen("settings")
}

//UI

val TITLES_SIZE = 20.sp
val NAVIGATION_ICONS_SIZE = 30.dp

val SPACER_HEIGHT_SIZE = 20.dp