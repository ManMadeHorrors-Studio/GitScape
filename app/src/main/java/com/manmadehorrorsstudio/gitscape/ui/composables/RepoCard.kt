package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.openBrowser
import com.manmadehorrorsstudio.gitscape.ui.theme.*


@Composable
fun RepoCard(
    gitHubRepo: GitHubRepo,
    isFullyVisible: Boolean
) {
    val progress = remember { Animatable(0f) }
    var firstTime by rememberSaveable() { mutableStateOf(true) }

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {openBrowser(gitHubRepo.htmlUrl, context)}
            )
            .drawWithContent {
                drawGlowBorder(
                    isFullyVisible = isFullyVisible,
                    progressValue = progress.value
                )
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = gitHubRepo.name,
                color = LightAzure,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                gitHubRepo.primaryLanguage?.let { language ->
                    Text(
                        text = language,
                        color = StrongAzure,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    tint = StrongAzure,
                    modifier = Modifier.size(25.dp)
                )

                Spacer(
                    modifier = Modifier.width(4.dp)
                )

                Text(
                    text = gitHubRepo.stars.toString(),
                    color = StrongAzure,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    LaunchedEffect(isFullyVisible) {
        if (isFullyVisible && firstTime) {
            firstTime = false

            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 2500,
                    easing = LinearEasing
                )
            )

            progress.snapTo(0f)
        } else {
            firstTime = true
            progress.snapTo(0f)
        }
    }
}

private fun ContentDrawScope.drawGlowBorder(
    isFullyVisible: Boolean,
    progressValue: Float
) {
    drawContent()

    if (isFullyVisible) {
        val width = size.width
        val height = size.height
        val glowWidth = width * 0.35f
        val stroke = 2.dp.toPx()
        val x = progressValue * (width + height * 2)

        drawRoundRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.Transparent,
                    StrongAzure,
                    LightAzure,
                    Color.Transparent
                ),
                start = Offset(x - glowWidth, 0f),
                end = Offset(x, 0f)
            ),
            size = size,
            cornerRadius = CornerRadius(32f, 32f),
            style = Stroke(width = stroke)
        )
    }
}