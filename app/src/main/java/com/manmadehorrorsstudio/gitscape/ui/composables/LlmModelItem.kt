package com.manmadehorrorsstudio.gitscape.ui.composables

import ai.koog.prompt.llm.LLModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun LlmModelItem(
    model: LLModel,
    selected: Boolean,
    onModelSelected: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onModelSelected(model.id) }
            )
            .background(
                color = if (selected) colors.primaryContainer else colors.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = model.id,
                style = MaterialTheme.typography.titleMedium,
                color = colors.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Context: ${model.contextLength ?: "—"} · Output: ${model.maxOutputTokens ?: "—"}",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant
            )
        }
        RadioButton(selected = selected, onClick = null)
    }
}