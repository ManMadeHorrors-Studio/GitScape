package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString

@Composable
fun ToolButton (
    onClick: () -> Unit,
    contentDescription: Int,
    imageVector: ImageVector? = null,
    painter: Int? = null
) {
    val shape = RoundedCornerShape(12.dp)
    val color = MaterialTheme.colorScheme

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(50.dp)
            .background(
                color = color.surface,
                shape = shape
            )
            .border(
                width = 2.dp,
                color = color.primary,
                shape = shape
            )
    ) {
        imageVector?.let { iv ->
            Icon(
                imageVector = iv,
                contentDescription = localizedString(contentDescription),
                tint = color.primary,
                modifier = Modifier.size(24.dp)
            )
        }
        painter?.let { p ->
            Icon(
                painter = painterResource(p),
                contentDescription = localizedString(contentDescription),
                tint = color.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}