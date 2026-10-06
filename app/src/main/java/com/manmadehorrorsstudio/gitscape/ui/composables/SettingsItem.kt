package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.ui.theme.*

@Composable
fun SettingsItem(
    text: String,
    image: Int,
    selected: Boolean,
    onClick: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = if(selected) LightAzure else DisabledAzure,
            fontSize = 16.sp,
            textAlign = TextAlign.Start
        )

        Spacer(
            modifier = Modifier.size(20.dp)
        )

        Image(
            painter = painterResource(image),
            contentDescription = text,
            modifier = Modifier
                .size(30.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun SettingsItemPreview() {
    SettingsItem(
        text = "Impostazioni",
        image = R.drawable.language_ita_logo,
        selected = true
    )
}