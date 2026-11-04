package com.manmadehorrorsstudio.gitscape

import android.app.Application
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalLlmRepository
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class GitScapeApplication : Application() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var localLlmRepository: LocalLlmRepository

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.Default).launch {
            settingsRepository.initDao()
            settingsRepository.observeSettings().collect { settings ->
                gitScapeLog("GitScapeApplication settings: $settings")
            }
        }
        CoroutineScope(Dispatchers.Default).launch {
            localLlmRepository.initDao()
            localLlmRepository.observeLlmConfiguration().collect { llmConfigurations ->
                gitScapeLog("GitScapeApplication llmConfigurations: $llmConfigurations")
            }
        }
    }
}