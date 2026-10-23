package com.manmadehorrorsstudio.gitscape

import ai.koog.prompt.executor.clients.google.GoogleModels
import androidx.compose.ui.unit.dp

//Remote

const val MAX_REPOS_NUMBER = 50
const val GITHUB_OWNER = "owner"
const val GITHUB_REPO = "repo"

//URLs

const val GITHUB_BASE_PATH = "github.com"

//Auth
const val GITHUB_LOGIN_PATH = "login/device/code"
const val GITHUB_GET_TOKEN_PATH = "login/oauth/access_token"

//Repo
const val GITHUB_API_BASE_PATH = "api.$GITHUB_BASE_PATH"
const val GITHUB_GET_REPOS_PATH = "user/repos"
const val GITHUB_GET_USER_PATH = "user"
const val GITHUB_GET_README = "repos/{$GITHUB_OWNER}/{$GITHUB_REPO}/readme"

//DB

const val GITSCAPE_DATABASE = "gitscape_database"
const val SETTINGS_TABLE_NAME = "settings_table"
const val GITHUB_REPOSITORIES_TABLE_NAME = "github_repositories_table"
const val TOKENS_TABLE_NAME = "tokens_table"

//Navigation

const val MAIN_NAVIGATION_GROUP = "MAIN_NAVIGATION_GROUP"

sealed class NavigationScreen(val route: String) {
    data object RepoList : NavigationScreen("repolist")
    data object Settings : NavigationScreen("settings")
}

//UI

val NAVIGATION_ICONS_SIZE = 30.dp

val SPACER_HEIGHT_SIZE = 20.dp

const val DYNAMIC_COLOR = true

//AI

//Set here the LLM provider you want to use
val LLM_MODEL = GoogleModels.Gemini3_7Flash

const val LIST_REPOS_NAME = "list_repos"

//GitHub

const val GITHUB_CLIENT_ID = "Iv23linPm3YUYyuhPuhm"