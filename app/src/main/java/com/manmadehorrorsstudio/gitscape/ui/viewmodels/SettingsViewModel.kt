package com.manmadehorrorsstudio.gitscape.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalGitHubRepository
import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage
import com.manmadehorrorsstudio.gitscape.ui.models.ColorStyle
import com.manmadehorrorsstudio.gitscape.ui.models.ThemeStyle
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel  @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val localGitHubRepository: LocalGitHubRepository
) : ViewModel() {

    fun getLanguageState() = settingsRepository.getLanguage()

    fun changeLanguage(language: AppLanguage) {
        viewModelScope.launch {
            settingsRepository.saveLanguage(language)
        }
    }

    fun getRepoListEnabledState() = settingsRepository.getRepoListEnabled()

    fun changeRepoListEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.saveRepoListEnabled(isEnabled)
            if (!isEnabled) {
                localGitHubRepository.clearGitHubRepositories()
            }
        }
    }

    fun getThemeStyle() = settingsRepository.getThemeStyle()

    fun changeThemeStyle(themeStyle: ThemeStyle) {
        viewModelScope.launch {
            settingsRepository.saveThemeStyle(themeStyle)
        }
    }

    fun getColorStyle() = settingsRepository.getColorStyle()

    fun changeColorStyle(colorStyle: ColorStyle) {
        viewModelScope.launch {
            settingsRepository.saveColorStyle(colorStyle)
        }
    }

    fun getGlowEffectOnScrollEnabledState() = settingsRepository.getGlowEffectOnScrollEnabled()

    fun changeGlowEffectOnScrollEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.saveGlowEffectOnScrollEnabled(isEnabled)
        }
    }
}