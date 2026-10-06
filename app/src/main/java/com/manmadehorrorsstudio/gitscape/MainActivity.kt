package com.manmadehorrorsstudio.gitscape

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.manmadehorrorsstudio.gitscape.ui.composables.GitScapeNavigation
import com.manmadehorrorsstudio.gitscape.ui.theme.GitScapeTheme
import com.manmadehorrorsstudio.gitscape.ui.theme.GrayBackground
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(GrayBackground.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(GrayBackground.toArgb())
        )

        setContent {
            GitScapeTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    containerColor = GrayBackground
                ) { innerPadding ->
                    val layoutDirection = LocalLayoutDirection.current
                    val customPadding = PaddingValues(
                        start = innerPadding.calculateStartPadding(layoutDirection) + 15.dp,
                        top = innerPadding.calculateTopPadding() + 10.dp,
                        end = innerPadding.calculateEndPadding(layoutDirection) + 15.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
                    GitScapeNavigation(customPadding)
                }
            }
        }
    }
}