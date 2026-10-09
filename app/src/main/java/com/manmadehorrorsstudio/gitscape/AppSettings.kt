package com.manmadehorrorsstudio.gitscape

import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettings @Inject constructor(
) {
    private val _appLanguageState = MutableStateFlow(AppLanguage.EN)
    val appLanguageState = _appLanguageState.asStateFlow()

    private val _repoListEnabled = MutableStateFlow(true)
    val repoListEnabled = _repoListEnabled.asStateFlow()

    fun setLanguageState(language: AppLanguage?) {
        language?.let { lang ->
            _appLanguageState.value = lang
        }
    }

    fun setRepoListEnabledState(isEnabled: Boolean) {
        _repoListEnabled.value = isEnabled
    }
}