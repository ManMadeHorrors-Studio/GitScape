package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString
import com.manmadehorrorsstudio.gitscape.ui.theme.*

@Composable
fun EmptyListFiller() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.github_sleepy),
            contentDescription = "gitHubUser.userName",
            modifier = Modifier.size(140.dp)
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = localizedString(R.string.REPOLISTVIEW_EMPTY_LIST),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = StrongAzure,
            textAlign = TextAlign.Center
        )
    }
}