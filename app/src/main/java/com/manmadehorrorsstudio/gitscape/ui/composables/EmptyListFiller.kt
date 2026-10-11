package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString

@Composable
fun EmptyListFiller() {
    val colors = MaterialTheme.colorScheme
    val emptyText = localizedString(R.string.REPOLISTVIEW_EMPTY_LIST)

    Column(
        modifier = Modifier
            .clearAndSetSemantics {
                contentDescription = emptyText
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .width(140.dp)
                .aspectRatio(497f / 447f)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_github_sleepy_body),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                colorFilter = ColorFilter.tint(
                    colors.primary
                )
            )

            Image(
                painter = painterResource(R.drawable.ic_github_sleepy_eyes),
                contentDescription = null,
                modifier = Modifier.matchParentSize()
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = emptyText,
            style = MaterialTheme.typography.bodyLarge,
            color = colors.primary,
            textAlign = TextAlign.Center
        )
    }
}