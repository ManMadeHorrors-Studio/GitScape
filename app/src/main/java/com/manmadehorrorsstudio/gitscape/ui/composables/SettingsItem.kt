package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.SPACER_HEIGHT_SIZE
import com.manmadehorrorsstudio.gitscape.ui.models.ColorStyle
import com.manmadehorrorsstudio.gitscape.ui.theme.GitScapeTheme

@Composable
fun SettingsItem(
    text: String,
    image: Int,
    color: Color? = null,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(top = 20.dp)
            .clearAndSetSemantics{
                contentDescription = text
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = if(selected) colors.secondary else colors.tertiary,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .clearAndSetSemantics{}
        )

        Spacer(
            modifier = Modifier.size(SPACER_HEIGHT_SIZE)
        )

        IconSettingsItem(image, 30, color)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun SettingsItemPreview() {
    GitScapeTheme(
        true,
        ColorStyle.AZURE_STYLE
    ) {
        SettingsItem(
            text = "Impostazioni",
            image = R.drawable.language_ita_logo,
            selected = true,
            onClick = {}
        )
    }
}