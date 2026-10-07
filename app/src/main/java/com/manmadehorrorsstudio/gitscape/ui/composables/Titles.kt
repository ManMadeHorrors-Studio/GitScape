package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.manmadehorrorsstudio.gitscape.ui.theme.LightAzure

@Composable
fun Title(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = LightAzure,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}