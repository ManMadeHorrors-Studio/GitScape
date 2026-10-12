package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp

@Composable
fun <T> ExpandableSelector(
    currentItem: T,
    items: List<T>,
    label: String,
    contDesc: String,
    itemText: @Composable (T) -> String,
    itemImage: (T) -> Int,
    itemColor: ((T) -> Color)? = null,
    onItemSelected: (T) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = !expanded
                }
                .clearAndSetSemantics {
                    contentDescription = contDesc
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = label,
                color = colors.primary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .clearAndSetSemantics{}
            )

            IconSettingsItem(itemImage(currentItem), 40, itemColor?.invoke(currentItem))
        }

        if (expanded) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 15.dp)
            ) {
                items.forEach { item ->
                    SettingsItem(
                        text = itemText(item),
                        image = itemImage(item),
                        color = itemColor?.invoke(item),
                        selected = currentItem == item,
                    ) {
                        onItemSelected(item)
                        expanded = false
                    }
                }
            }
        }
    }
}
