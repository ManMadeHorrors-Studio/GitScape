package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString

@Composable
fun AlertMessage(
    errorAlert: MutableState<Pair<Int?, (() -> Unit)?>?>,
    title: String = localizedString(R.string.GENERIC_ERROR_TITLE)
) {
    errorAlert.value?.let { (message, onConfirm) ->
        message?.let { mess ->
            AlertDialog(
                onDismissRequest = {
                    errorAlert.value = null
                },
                title = {
                    Text(
                        text = title
                    )
                },
                text = {
                    Text(
                        text = localizedString(mess)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            errorAlert.value = null
                            onConfirm?.invoke()
                        }
                    ) {
                        Text(
                            text = localizedString(R.string.GENERIC_ERROR_OK)
                        )
                    }
                },
                dismissButton = onConfirm?.let {
                    {
                        OutlinedButton(
                            onClick = {
                                errorAlert.value = null
                            }
                        ) {
                            Text(
                                text = localizedString(R.string.GENERIC_ERROR_CANCEL)
                            )
                        }
                    }
                }
            )
        }
    }
}