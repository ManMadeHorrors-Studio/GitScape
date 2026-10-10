package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CircularLoader(size: Int = 60){
    val colors = MaterialTheme.colorScheme
    CircularProgressIndicator(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .wrapContentWidth()
            .size(size.dp),
        strokeWidth = if(size > 30) 8.dp else 4.dp,
        color = colors.primary
    )
}
