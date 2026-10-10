package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manmadehorrorsstudio.gitscape.R


@Composable
fun RepoListToolbar(
    onAIClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToolButton(
            onClick = onAIClick,
            contentDescription = R.string.REPOLISTVIEW_BUTTON_AI_CONTENTDESCRIPTION,
            painter = R.drawable.ai_icon
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        ToolButton(
            onClick = onRefreshClick,
            contentDescription = R.string.REPOLISTVIEW_BUTTON_REFRESH_CONTENTDESCRIPTION,
            imageVector = Icons.Default.Refresh
        )

        Spacer(
            modifier = Modifier.width(15.dp)
        )

        ToolButton(
            onClick = onSettingsClick,
            contentDescription = R.string.REPOLISTVIEW_BUTTON_SETTINGS_CONTENTDESCRIPTION,
            imageVector = Icons.Default.Settings
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun RepoListToolbarPreview() {
    RepoListToolbar(
        {},
        {},
        {}
    )
}