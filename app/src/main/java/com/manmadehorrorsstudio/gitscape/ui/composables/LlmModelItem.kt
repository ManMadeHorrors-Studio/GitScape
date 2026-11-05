package com.manmadehorrorsstudio.gitscape.ui.composables

import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.llm.LLModel
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manmadehorrorsstudio.gitscape.SPACER_HEIGHT_SIZE
import com.manmadehorrorsstudio.gitscape.capitalizeFirst
import com.manmadehorrorsstudio.gitscape.drawGlowBorder
import com.manmadehorrorsstudio.gitscape.ui.models.ColorStyle
import com.manmadehorrorsstudio.gitscape.ui.theme.GitScapeTheme

@Composable
fun LlmModelItem(
    model: LLModel,
    selected: Boolean,
    onModelSelected: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val progress = remember {
        Animatable(0f)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onModelSelected(model.id) }
            )
            .background(
                color = if (selected) colors.primaryContainer else colors.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
            .drawWithContent {
                drawGlowBorder(
                    isHighlighted = selected,
                    progressValue = progress.value,
                    colors = colors
                )
            }
            .padding(15.dp)

    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = model.id.capitalizeFirst(),
                style = MaterialTheme.typography.titleMedium,
                color = colors.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            var subText = ""
            model.contextLength?.let { ctxl ->
                subText = "Context: $ctxl"
            }
            model.maxOutputTokens?.let { mot ->
                if (subText.isNotBlank())
                    subText += " - "
                subText += "Output: $mot"
            }

            if (subText.isNotBlank()) {
                Spacer(Modifier.height(SPACER_HEIGHT_SIZE))
                Text(
                    text = subText,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )
            }
        }
        RadioButton(selected = selected, onClick = null)
    }
    LaunchedEffect(selected) {
        if (selected) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 1500,
                    easing = LinearEasing
                )
            )
            progress.snapTo(0f)
        } else {
            progress.snapTo(0f)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LlmModelItemPreview() {
    GitScapeTheme(
        isDarkTheme = true,
        colorStyle = ColorStyle.GREEN_STYLE
    ) {
        LlmModelItem(
            model = OpenAIModels.Chat.GPT4_1Mini,
            selected = true,
            onModelSelected = {}
        )
    }
}