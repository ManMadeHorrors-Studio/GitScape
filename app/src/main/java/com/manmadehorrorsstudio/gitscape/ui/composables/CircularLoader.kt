package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.manmadehorrorsstudio.gitscape.ui.theme.StrongAzure

@Composable
fun CircularLoader(modifier: Modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp).wrapContentWidth()){
    val mod = modifier.size(60.dp)
    CircularProgressIndicator(
        modifier = mod,
        strokeWidth = 8.dp,
        color = StrongAzure
    )
}
