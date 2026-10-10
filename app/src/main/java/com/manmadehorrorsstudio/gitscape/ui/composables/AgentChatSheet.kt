package com.manmadehorrorsstudio.gitscape.ui.composables

import ai.koog.prompt.message.Message.Role
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.SPACER_HEIGHT_SIZE
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString
import com.manmadehorrorsstudio.gitscape.ui.models.toText
import com.manmadehorrorsstudio.gitscape.ui.viewmodels.AgentViewModel

@Composable
fun AgentChatSheet(
    agentViewModel: AgentViewModel
) {
    val agentState by agentViewModel.agentState.collectAsStateWithLifecycle()
    var currentInput by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val textFieldShape = RoundedCornerShape(16.dp)

    val launchSpeech = speechLauncher(
        onReadedText = { readedText -> agentViewModel.send(readedText) },
        hint = localizedString(R.string.AGENTSHEET_INPUT_HINT),
        languageTag = agentViewModel.getCurrentLanguageTag()
    )

    val listState = rememberLazyListState()

    val colors = MaterialTheme.colorScheme

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .background(
                color = colors.onPrimary,
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp)
            )
            .clip(RoundedCornerShape(24.dp))

    ) {
        Spacer(Modifier.height(SPACER_HEIGHT_SIZE))

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Title(
                    title = localizedString(R.string.AGENTSHEET_TITLE)
                )

                Spacer(Modifier.width(5.dp))

                Icon(
                    painter = painterResource(R.drawable.ai_icon),
                    contentDescription = localizedString(R.string.REPOLISTVIEW_BUTTON_AI_CONTENTDESCRIPTION),
                    tint = colors.primary,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.height(SPACER_HEIGHT_SIZE))

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f, fill = false)
                    .fillMaxWidth()
            ) {
                val messModifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 5.dp
                    )
                items(agentState.messages) { message ->
                    if (message.role == Role.User)
                        Text(
                            text = "${localizedString(R.string.AGENTSHEET_CHAT_YOU)}  ${message.toText()}",
                            color = colors.primary,
                            textAlign = TextAlign.End,
                            modifier = messModifier
                        )
                    else
                        Text(
                            text = "${localizedString(R.string.AGENTSHEET_CHAT_AI)}  ${message.toText()}",
                            color = colors.secondary,
                            textAlign = TextAlign.Start,
                            modifier = messModifier
                        )
                }
                if (agentState.isLoading)
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = messModifier
                        ) {
                            CircularLoader(18)

                            Spacer(
                                modifier = Modifier.width(10.dp)
                            )

                            Text(
                                localizedString(R.string.AGENTSHEET_THINKING),
                                color = colors.primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                        }
                    }

                agentState.error?.let { err ->
                    item {
                        Text(
                            err,
                            color = MaterialTheme.colorScheme.error,
                            modifier = messModifier
                        )
                    }
                }
            }

            Spacer(Modifier.height(SPACER_HEIGHT_SIZE))

            TextField(
                value = currentInput,
                onValueChange = {
                    currentInput = it

                },

                enabled = !agentState.isLoading,

                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = colors.primary,
                        shape = textFieldShape
                    ),

                shape = textFieldShape,


                placeholder = {
                    Text(
                        text =
                            if (!agentState.isLoading)
                                localizedString(R.string.AGENTSHEET_INPUT_HINT)
                            else
                                "",
                        color = colors.tertiary
                    )
                },

                singleLine = true,

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colors.surfaceContainerHigh,
                    unfocusedContainerColor = colors.surfaceContainerHigh,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    disabledContainerColor = colors.surfaceContainerLow,

                    cursorColor = colors.primary,
                    focusedTextColor = colors.secondary,
                    unfocusedTextColor = colors.secondary
                ),

                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),

                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        agentViewModel.send(currentInput)
                        currentInput = ""
                    }
                ),

                trailingIcon = {
                    Row {
                        if (currentInput.isNotBlank()) {
                            IconButton(
                                onClick = { currentInput = "" }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = localizedString(R.string.SEARCH_CLEAR),
                                    tint = colors.primary
                                )
                            }
                        } else
                            IconButton(
                                onClick = launchSpeech
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.mic_icon),
                                    contentDescription = localizedString(R.string.SEARCH_VOICE),
                                    tint = colors.primary
                                )
                            }
                    }
                }
            )

            Spacer(Modifier.height(SPACER_HEIGHT_SIZE))
        }
    }

    LaunchedEffect(agentState.messages.size) {
        if(agentState.messages.lastIndex > 0)
            listState.animateScrollToItem(agentState.messages.lastIndex)
    }
}

