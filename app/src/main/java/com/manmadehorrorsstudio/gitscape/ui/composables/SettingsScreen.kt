package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manmadehorrorsstudio.gitscape.BuildConfig
import com.manmadehorrorsstudio.gitscape.NAVIGATION_ICONS_SIZE
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString
import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage
import com.manmadehorrorsstudio.gitscape.ui.theme.*
import com.manmadehorrorsstudio.gitscape.ui.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    innerPadding: PaddingValues,
    onBackClick: () -> Unit
) {
    val currentLanguage by settingsViewModel.getLanguageState().collectAsState()
    val currentRepoListEnabled by settingsViewModel.getRepoListEnabledState().collectAsState()

    SettingsScreenContent(
        title = localizedString(R.string.SETTINGSVIEW_TITLE),
        innerPadding = innerPadding,
        languageLabel = localizedString(R.string.SETTINGSVIEW_LIST_LANGUAGE),
        languageContentDescription = localizedString(R.string.SETTINGSVIEW_LIST_LANGUAGE_CONTENTDESCRIPTION),
        currentLanguage = currentLanguage,
        currentRepoListEnabledLabel = localizedString(R.string.SETTINGSVIEW_REPOLIST_ENABLED),
        currentRepoListEnabled = currentRepoListEnabled,
        versionString = getVersionString(),
        onBackClick = onBackClick,
        onLanguageSelected = { language ->
            settingsViewModel.changeLanguage(language)
        },
        onRepoListEnabledSelected = { isEnabled ->
            settingsViewModel.changeRepoListEnabled(isEnabled)
        }
    )
}

@Composable
private fun SettingsScreenContent(
    title: String,
    innerPadding: PaddingValues,
    languageLabel: String,
    languageContentDescription: String,
    currentLanguage: AppLanguage,
    currentRepoListEnabledLabel: String,
    currentRepoListEnabled: Boolean,
    versionString: String,
    onBackClick: () -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
    onRepoListEnabledSelected: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)

            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = localizedString(R.string.GENERIC_BACK),
                    tint = StrongAzure,
                    modifier = Modifier
                        .size(NAVIGATION_ICONS_SIZE)
                )
            }

            Title(
                title = title,
                modifier = Modifier
                    .padding(horizontal = NAVIGATION_ICONS_SIZE + 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        HorizontalDivider(
            color = StrongAzure
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            ExpandableSelector(
                currentItem = currentLanguage,
                items = AppLanguage.entries,
                label = languageLabel,
                contentDescription = languageContentDescription,
                itemText = { language ->
                    localizedString(language.description)
                },
                itemImage = { language ->
                    language.flag
                },
                onItemSelected = { language ->
                    onLanguageSelected(language)
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onRepoListEnabledSelected(!currentRepoListEnabled)
                    }
            ) {

                Text(
                    text = currentRepoListEnabledLabel,
                    color = LightAzure,
                    fontSize = 18.sp,
                    modifier = Modifier,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.weight(1f))

                Switch(
                    checked = currentRepoListEnabled,
                    onCheckedChange = null
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = versionString,
                color = StrongAzure,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun <T> ExpandableSelector(
    currentItem: T,
    items: List<T>,
    label: String,
    contentDescription: String,
    itemText: @Composable (T) -> String,
    itemImage: (T) -> Int,
    onItemSelected: (T) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = !expanded
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = label,
                color = LightAzure,
                fontSize = 18.sp
            )

            Image(
                painter = painterResource(itemImage(currentItem)),
                contentDescription = contentDescription,
                modifier = Modifier.size(40.dp)
            )
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
                        selected = currentItem == item
                    ) {
                        onItemSelected(item)
                        expanded = false
                    }
                }
            }
        }
    }
}

@Composable
private fun getVersionString(): String {
    val packageInfo = LocalContext.current.packageManager.getPackageInfo(LocalContext.current.packageName, 0)
    val versionCode = packageInfo.longVersionCode
    val versionName = packageInfo.versionName

    val versionString = "${localizedString(R.string.SETTINGSVIEW_VERSION)}: ${BuildConfig.BUILD_TYPE.replaceFirstChar { it.uppercase()}} $versionCode ($versionName)"
    gitScapeLog(versionString)
    return versionString
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreenContent(
        title = "Settings",
        innerPadding = PaddingValues(15.dp),
        languageLabel = "Language",
        languageContentDescription = "Select Language",
        currentLanguage = AppLanguage.EN,
        currentRepoListEnabledLabel = "Local Repositories",
        currentRepoListEnabled = true,
        versionString = "Version Debug 666 (6.6.6)",
        onBackClick = {},
        onLanguageSelected = {},
        onRepoListEnabledSelected = {},
    )
}