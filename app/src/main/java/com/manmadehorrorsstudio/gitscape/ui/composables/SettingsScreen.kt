package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manmadehorrorsstudio.gitscape.BuildConfig
import com.manmadehorrorsstudio.gitscape.NAVIGATION_ICONS_SIZE
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString
import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage
import com.manmadehorrorsstudio.gitscape.ui.models.AuthStatus
import com.manmadehorrorsstudio.gitscape.ui.models.ColorStyle
import com.manmadehorrorsstudio.gitscape.ui.models.ThemeStyle
import com.manmadehorrorsstudio.gitscape.ui.viewmodels.SettingsViewModel
import com.manmadehorrorsstudio.gitscape.ui.theme.GitScapeTheme
import com.manmadehorrorsstudio.gitscape.ui.viewmodels.AuthViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    authViewModel: AuthViewModel,
    innerPadding: PaddingValues,
    onBackClick: () -> Unit
) {
    val currentLanguage by settingsViewModel.getLanguageState().collectAsState()
    val currentRepoListEnabled by settingsViewModel.getRepoListEnabledState().collectAsState()
    val currentThemeStyle by settingsViewModel.getThemeStyle().collectAsState()
    val currentColorStyle by settingsViewModel.getColorStyle().collectAsState()
    val currentGlowEffectOnScrollEnabled by settingsViewModel.getGlowEffectOnScrollEnabledState().collectAsState()
    val authState by authViewModel.authState.collectAsState()

    SettingsScreenContent(
        title = localizedString(R.string.SETTINGSVIEW_TITLE),
        innerPadding = innerPadding,
        languageLabel = localizedString(R.string.SETTINGSVIEW_LIST_LANGUAGE),
        languageContentDescription = localizedString(R.string.SETTINGSVIEW_LIST_LANGUAGE_CONTENTDESCRIPTION),
        currentLanguage = currentLanguage,
        currentRepoListEnabledLabel = localizedString(R.string.SETTINGSVIEW_REPOLIST_ENABLED),
        currentRepoListEnabled = currentRepoListEnabled,
        currentThemeStyle = currentThemeStyle,
        currentColorStyle = currentColorStyle,
        themeStyleLabel = localizedString(R.string.SETTINGSVIEW_THEME),
        glowEffectOnScrollEnabledLabel = localizedString(R.string.SETTINGSVIEW_GLOW_ENABLED),
        currentGlowEffectOnScrollEnabled = currentGlowEffectOnScrollEnabled,
        userRegistered = authState.authStatus == AuthStatus.VALIDATED || authState.authStatus == AuthStatus.OFFLINE,
        versionString = getVersionString(),
        onBackClick = onBackClick,
        onLanguageSelected = { language ->
            settingsViewModel.changeLanguage(language)
        },
        onRepoListEnabledSelected = { isEnabled ->
            settingsViewModel.changeRepoListEnabled(isEnabled)
        },
        onColorStyleSelected = { colorStyle ->
            settingsViewModel.changeColorStyle(colorStyle)
        },
        onThemeStyleSelected = { themeStyle ->
            settingsViewModel.changeThemeStyle(themeStyle)
        },
        onGlowEffectOnScrollEnabledSelected = { isEnabled ->
            settingsViewModel.changeGlowEffectOnScrollEnabled(isEnabled)
        },
        onClearUserConfirmed = {
            authViewModel.setNoUserRegistered()
            authViewModel.clearState()
            onBackClick()
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
    currentColorStyle: ColorStyle,
    themeStyleLabel: String,
    currentThemeStyle: ThemeStyle,
    glowEffectOnScrollEnabledLabel: String,
    currentGlowEffectOnScrollEnabled: Boolean,
    userRegistered: Boolean,
    versionString: String,
    onBackClick: () -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
    onRepoListEnabledSelected: (Boolean) -> Unit,
    onThemeStyleSelected: (ThemeStyle) -> Unit,
    onColorStyleSelected: (ColorStyle) -> Unit,
    onGlowEffectOnScrollEnabledSelected: (Boolean) -> Unit,
    onClearUserConfirmed: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val types = MaterialTheme.typography
    val showSettingsAlert = remember { mutableStateOf<Pair<Int?, (() -> Unit)?>?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(colors.background)
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
                    tint = colors.primary,
                    modifier = Modifier
                        .size(NAVIGATION_ICONS_SIZE)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Title(
                title = title,
                modifier = Modifier
                    .padding(horizontal = NAVIGATION_ICONS_SIZE + 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        HorizontalDivider(
            color = colors.primary
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
                contDesc = languageContentDescription,
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
                    color = colors.primary,
                    style = types.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Switch(
                    checked = currentRepoListEnabled,
                    onCheckedChange = null
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            ExpandableSelector(
                currentItem = currentThemeStyle,
                items = ThemeStyle.entries,
                label = themeStyleLabel,
                contDesc = themeStyleLabel,
                itemText = { themeStyle ->
                    localizedString(themeStyle.description)
                },
                itemImage = { themeStyle ->
                    themeStyle.icon
                },
                itemColor = { colors.primary },
                onItemSelected = onThemeStyleSelected
            )

            Spacer(modifier = Modifier.height(30.dp))

            ExpandableSelector(
                currentItem = currentColorStyle,
                items = ColorStyle.entries,
                label = localizedString(R.string.SETTINGSVIEW_COLORSTYLE),
                contDesc = localizedString(R.string.SETTINGSVIEW_COLORSTYLE),
                itemText = { color ->
                    localizedString(color.description)
                },
                itemImage = {
                    R.drawable.ic_color_setting
                },
                itemColor = { colorStyle ->
                    colorStyle.representiveColor
                },
                onItemSelected = { colorStyle ->
                    onColorStyleSelected(colorStyle)
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onGlowEffectOnScrollEnabledSelected(!currentGlowEffectOnScrollEnabled)
                    }
            ) {
                Text(
                    text = glowEffectOnScrollEnabledLabel,
                    color = colors.primary,
                    style = types.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Switch(
                    checked = currentGlowEffectOnScrollEnabled,
                    onCheckedChange = null
                )
            }

            if (userRegistered) {
                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showSettingsAlert.value =
                                Pair(R.string.SETTINGSVIEW_CLEAR_USER_WARNING, onClearUserConfirmed)
                        }
                ) {
                    Text(
                        text = localizedString(R.string.SETTINGSVIEW_CLEAR_USER_DESCRIPTION),
                        color = colors.primary,
                        style = types.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    IconSettingsItem(R.drawable.ic_delete, 40, colors.primary)
                }
            }

            AlertMessage(errorAlert = showSettingsAlert)

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = versionString,
                color = colors.primary,
                style = types.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                textAlign = TextAlign.Center
            )
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
    GitScapeTheme(
        true,
        ColorStyle.GREEN_STYLE
    ) {
        SettingsScreenContent(
            title = "Settings",
            innerPadding = PaddingValues(15.dp),
            languageLabel = "Language",
            languageContentDescription = "Select Language",
            currentLanguage = AppLanguage.EN,
            currentRepoListEnabledLabel = "Local Repositories",
            currentRepoListEnabled = true,
            glowEffectOnScrollEnabledLabel = "Scroll glow effect",
            currentGlowEffectOnScrollEnabled = true,
            userRegistered = true,
            currentColorStyle = ColorStyle.AZURE_STYLE,
            currentThemeStyle = ThemeStyle.DEFAULT,
            themeStyleLabel = "Theme",
            versionString = "Version Debug 666 (6.6.6)",
            onBackClick = {},
            onLanguageSelected = {},
            onRepoListEnabledSelected = {},
            onThemeStyleSelected = {},
            onGlowEffectOnScrollEnabledSelected = {},
            onColorStyleSelected = {},
            onClearUserConfirmed = {}
        )
    }
}