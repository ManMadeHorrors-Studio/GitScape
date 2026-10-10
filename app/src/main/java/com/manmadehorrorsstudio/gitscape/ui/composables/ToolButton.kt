package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString
import com.manmadehorrorsstudio.gitscape.ui.theme.CardBackground
import com.manmadehorrorsstudio.gitscape.ui.theme.StrongAzure

@Composable
fun ToolButton (
    onClick: () -> Unit,
    contentDescription: Int,
    imageVector: ImageVector? = null,
    painter: Int? = null
) {
    val shape = RoundedCornerShape(12.dp)

    IconButton(
        onClick = onClick,
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
        imageVector?.let { iv ->
            Icon(
                imageVector = iv,
                contentDescription = localizedString(contentDescription),
                tint = StrongAzure,
                modifier = Modifier.size(24.dp)
            )
        }
        painter?.let { p ->
            Icon(
                painter = painterResource(p),
                contentDescription = localizedString(contentDescription),
                tint = StrongAzure,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}