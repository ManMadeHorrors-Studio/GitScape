package com.manmadehorrorsstudio.gitscape.ui.managers

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.LocaleList
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.manmadehorrorsstudio.gitscape.di.AppSettingsEntryPoint
import dagger.hilt.android.EntryPointAccessors

@SuppressLint("LocalContextConfigurationRead")
@Composable
fun localizedString(@StringRes id: Int): String {

    val context = LocalContext.current

    val entryPoint = EntryPointAccessors.fromApplication(
        context.applicationContext,
        AppSettingsEntryPoint::class.java
    )

    val appSettings = entryPoint.appSettings()

    val language by appSettings.appLanguageState.collectAsState()

    val config = Configuration(context.resources.configuration).apply {
        setLocales(LocaleList(language.locale))
    }

    val localizedContext = context.createConfigurationContext(config)

    return localizedContext.getString(id)
}