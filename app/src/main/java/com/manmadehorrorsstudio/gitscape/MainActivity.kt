package com.manmadehorrorsstudio.gitscape

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.manmadehorrorsstudio.gitscape.ui.composables.GitScapeNavigation
import com.manmadehorrorsstudio.gitscape.ui.theme.GitScapeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkTheme = isSystemInDarkTheme()

            GitScapeTheme(
                isDarkTheme = isDarkTheme
            ) {
                val colors = MaterialTheme.colorScheme

                SideEffect {
                    val barStyle =
                        if (isDarkTheme) {
                            SystemBarStyle.dark(colors.background.toArgb())
                        } else {
                            SystemBarStyle.light(
                                colors.background.toArgb(),
                                colors.background.toArgb()
                            )
                        }

                    enableEdgeToEdge(
                        statusBarStyle = barStyle,
                        navigationBarStyle = barStyle
                    )
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    containerColor = colors.background
                ) { innerPadding ->
                    val layoutDirection = LocalLayoutDirection.current
                    val customPadding = PaddingValues(
                        start = innerPadding.calculateStartPadding(layoutDirection) + 15.dp,
                        top = innerPadding.calculateTopPadding() + 20.dp,
                        end = innerPadding.calculateEndPadding(layoutDirection) + 15.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
                    GitScapeNavigation(customPadding)
                }
            }
        }
    }
}