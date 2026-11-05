package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.manmadehorrorsstudio.gitscape.NAVIGATION_ICONS_SIZE
import com.manmadehorrorsstudio.gitscape.getLogo
import com.manmadehorrorsstudio.gitscape.ui.viewmodels.LlmConfigurationViewModel

@Composable
fun LlmConfigurationScreen(
    llmConfigurationViewModel: LlmConfigurationViewModel,
    innerPadding: PaddingValues,
    onBackClick: () -> Unit
) {
    val selectedLlmConfigurationState by llmConfigurationViewModel.selectedLlmConfigurationState.collectAsStateWithLifecycle()

    val models = llmConfigurationViewModel.getModelsForConfiguration()

    val listState = rememberLazyListState()

    var aiKeyWriting by remember { mutableStateOf("") }

    val colors = MaterialTheme.colorScheme
    val types = MaterialTheme.typography
    val textFieldShape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
    ) {
        selectedLlmConfigurationState?.let { slcs ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(NAVIGATION_ICONS_SIZE)
                    )
                }

                Title(
                    title = slcs.llmProvider.display,
                    modifier = Modifier.weight(1f)
                )

                IconSettingsItem(slcs.getLogo(), 40)

                Spacer(modifier = Modifier.size(NAVIGATION_ICONS_SIZE))
            }

            HorizontalDivider(color = colors.primary)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (slcs.aiKey == null) {
                    TextField(
                        value = aiKeyWriting,
                        onValueChange = { aiKeyWriting = it },
                        modifier = Modifier
                            .weight(1f)
                            .border(2.dp, colors.primary, textFieldShape),
                        shape = textFieldShape,
                        placeholder = {
                            Text(
                                text = localizedString(R.string.LLM_SETTINGSVIEW_KEY_HINT),
                                color = colors.tertiary,
                                style = types.labelMedium
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colors.surfaceContainerHigh,
                            unfocusedContainerColor = colors.surfaceContainerHigh,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = colors.primary,
                            focusedTextColor = colors.secondary,
                            unfocusedTextColor = colors.secondary
                        )
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(onClick = {
                        llmConfigurationViewModel.saveAiKey(aiKeyWriting)
                    }) {
                        Text(localizedString(R.string.LLM_SETTINGSVIEW_KEY_BUTTON_INSERT))
                    }
                } else {
                    Text(
                        text = localizedString(R.string.LLM_SETTINGSVIEW_KEY_INSERTED),
                        modifier = Modifier.weight(1f),
                        color = colors.onBackground,
                        style = types.bodyLarge
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(onClick = {
                        aiKeyWriting = ""
                        llmConfigurationViewModel.saveAiKey(null)
                    }) {
                        Text(localizedString(R.string.LLM_SETTINGSVIEW_KEY_BUTTON_DELETE))
                    }
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(models, key = { it.id }) { model ->
                    LlmModelItem(
                        model = model,
                        selected = model.id == slcs.selectedModel,
                        onModelSelected = { selectedModel ->
                            llmConfigurationViewModel.saveModel(selectedModel)
                        }
                    )
                }
            }

            LaunchedEffect(slcs.selectedModel) {
                if (slcs.selectedModel != null) {
                    listState.animateScrollToItem(0)
                }
            }
        }
    }
}