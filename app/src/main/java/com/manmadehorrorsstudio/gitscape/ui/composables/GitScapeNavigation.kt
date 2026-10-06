package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.manmadehorrorsstudio.gitscape.MAIN_NAVIGATION_GROUP
import com.manmadehorrorsstudio.gitscape.NavigationScreen
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.viewmodels.RepoListViewModel
import com.manmadehorrorsstudio.gitscape.ui.viewmodels.SettingsViewModel


@Composable
fun GitScapeNavigation(innerPadding: PaddingValues) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MAIN_NAVIGATION_GROUP
    ) {
        @Composable
        fun getParentEntry(
            backStackEntry: NavBackStackEntry,
            parentRoute: String
        ): NavBackStackEntry {
            return remember(backStackEntry) {
                navController.getBackStackEntry(parentRoute)
            }
        }

        navigation(
            startDestination = NavigationScreen.RepoList.route,
            route = MAIN_NAVIGATION_GROUP
        ) {
            composable(NavigationScreen.RepoList.route) { backStackEntry ->

                val parentEntry = getParentEntry(
                    backStackEntry,
                    MAIN_NAVIGATION_GROUP
                )
                val repoListViewModel: RepoListViewModel = hiltViewModel(parentEntry)

                RepoListScreen(
                    repoListViewModel = repoListViewModel,
                    innerPadding = innerPadding,
                    onSettingsClick = {
                        navController.navigate(NavigationScreen.Settings.route)
                    }
                )
            }

            composable(NavigationScreen.Settings.route) { backStackEntry ->

                val parentEntry = getParentEntry(
                    backStackEntry,
                    MAIN_NAVIGATION_GROUP
                )
                val settingsViewModel: SettingsViewModel = hiltViewModel(parentEntry)

                SettingsScreen(
                    settingsViewModel = settingsViewModel,
                    innerPadding = innerPadding,
                    onBackClick = {
                        gitScapeLog("Tapped SettingsScreen onBackClick")
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}