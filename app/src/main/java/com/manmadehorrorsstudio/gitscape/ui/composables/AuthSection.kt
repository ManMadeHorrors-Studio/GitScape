package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString
import com.manmadehorrorsstudio.gitscape.ui.models.AuthState

@Composable
fun AuthSection (
    authState: AuthState,
    authAdvanceStep: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val types = MaterialTheme.typography

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        IconSettingsItem(R.drawable.ic_github_main, 240, colors.primary)

        Spacer(modifier = Modifier.height(20.dp))

        authState.authStatus.message?.let { message ->
            Text(
                text = localizedString(message),
                textAlign = TextAlign.Center,
                color = colors.primary,
                style = types.bodyMedium,
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        authState.authDeviceCode?.userCode?.let { userCode ->
            SelectionContainer {
                Text(
                    text = userCode,
                    color = colors.primary,
                    style = types.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }

        authState.authStatus.buttonMessage?.let { bm ->
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { authAdvanceStep() }
            ) {
                Text(
                    text = localizedString(bm),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

