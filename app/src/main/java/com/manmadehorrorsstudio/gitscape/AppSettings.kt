package com.manmadehorrorsstudio.gitscape

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettings @Inject constructor(
) {
    private val _appLanguageState = MutableStateFlow(AppLanguage.EN)
    val appLanguageState = _appLanguageState.asStateFlow()

    fun setLanguageState(language: AppLanguage?) {
        language?.let { lang ->
            _appLanguageState.value = lang
        }
    }
}