package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString
import com.manmadehorrorsstudio.gitscape.ui.theme.*


@Composable
fun RepoListToolbar(
    onAIClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onAIClick,
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = CardBackground,
                    shape = shape
                )
                .border(
                    width = 2.dp,
                    color = StrongAzure,
                    shape = shape
                )
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,  //TODO mettere un'icona di AI
                contentDescription = localizedString(R.string.REPOLISTVIEW_BUTTON_AI_CONTENTDESCRIPTION),
                tint = StrongAzure,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )


        IconButton(
            onClick = onRefreshClick,
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = CardBackground,
                    shape = shape
                )
                .border(
                    width = 2.dp,
                    color = StrongAzure,
                    shape = shape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = localizedString(R.string.REPOLISTVIEW_BUTTON_REFRESH_CONTENTDESCRIPTION),
                tint = StrongAzure,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(15.dp)
        )

        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = CardBackground,
                    shape = shape
                )
                .border(
                    width = 2.dp,
                    color = StrongAzure,
                    shape = shape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = localizedString(R.string.REPOLISTVIEW_BUTTON_SETTINGS_CONTENTDESCRIPTION),
                tint = StrongAzure,
                modifier = Modifier.size(24.dp)
            )
        }
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