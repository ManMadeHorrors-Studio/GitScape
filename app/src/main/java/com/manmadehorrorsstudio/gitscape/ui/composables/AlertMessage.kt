package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString
import com.manmadehorrorsstudio.gitscape.ui.theme.StrongAzure


@Composable
fun AlertMessage(
    alertMessage: MutableState<String?>,
    title: String = localizedString(R.string.GENERIC_ERROR_TITLE)
) {
    alertMessage.value?.let { am ->

        AlertDialog(
            onDismissRequest = {
                alertMessage.value = null
            },
            title = {
                Text(
                    text = title
                )
            },
            text = {
                Text(
                    text = am
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        alertMessage.value = null
                    }
                ) {
                    Text(
                        text = localizedString(R.string.GENERIC_ERROR_OK),
                        color = StrongAzure
                    )
                }
            }
        )
    }
}