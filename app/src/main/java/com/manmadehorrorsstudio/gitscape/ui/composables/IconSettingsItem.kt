package com.manmadehorrorsstudio.gitscape.ui.composables


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp

@Composable
fun IconSettingsItem(image: Int, diametro: Int, color: Color? = null) {
    Image(
        painter = painterResource(image),
        contentDescription = null,
        colorFilter = color?.let {ColorFilter.tint(it)},
        modifier = Modifier
            .size(diametro.dp)
            .clearAndSetSemantics{}
    )
}