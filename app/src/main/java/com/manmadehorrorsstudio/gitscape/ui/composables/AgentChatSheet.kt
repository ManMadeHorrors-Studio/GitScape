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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.SPACER_HEIGHT_SIZE
import com.manmadehorrorsstudio.gitscape.TITLES_SIZE
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString
import com.manmadehorrorsstudio.gitscape.ui.theme.*
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

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .background(
                color = GrayBackground,
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

                Text(
                    text = localizedString(R.string.AGENTSHEET_TITLE),
                    fontSize = TITLES_SIZE,
                    fontWeight = FontWeight.Bold,
                    color = LightAzure,
                )

                Spacer(Modifier.width(5.dp))

                Icon(
                    painter = painterResource(R.drawable.ai_icon),
                    contentDescription = localizedString(R.string.REPOLISTVIEW_BUTTON_AI_CONTENTDESCRIPTION),
                    tint = StrongAzure,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.height(SPACER_HEIGHT_SIZE))

            LazyColumn(
                Modifier
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
                            text = "${localizedString(R.string.AGENTSHEET_CHAT_YOU)}  ${message.text}",
                            color = StrongAzure,
                            textAlign = TextAlign.End,
                            modifier = messModifier
                        )
                    else
                        Text(
                            text = "${localizedString(R.string.AGENTSHEET_CHAT_AI)}  ${message.text}",
                            color = LightAzure,
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
                            CircularProgressIndicator(
                                color = StrongAzure,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(
                                modifier = Modifier.width(10.dp)
                            )

                            Text(
                                localizedString(R.string.AGENTSHEET_THINKING),
                                color = StrongAzure
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
                        color = StrongAzure,
                        shape = textFieldShape
                    ),

                shape = textFieldShape,

                placeholder = {
                    Text(
                        text = localizedString(R.string.AGENTSHEET_INPUT_HINT),
                        color = Color.Gray
                    )
                },

                singleLine = true,

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    disabledContainerColor = DisabledAzure,

                    cursorColor = StrongAzure,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
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
                                    tint = LightAzure
                                )
                            }
                        } else
                            IconButton(
                                onClick = launchSpeech//{speechLauncher.launch(speechIntent)}
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.mic_icon),
                                    contentDescription = localizedString(R.string.SEARCH_VOICE),
                                    tint = LightAzure
                                )
                            }
                    }
                }
            )

            Spacer(Modifier.height(SPACER_HEIGHT_SIZE))
        }
    }
}

