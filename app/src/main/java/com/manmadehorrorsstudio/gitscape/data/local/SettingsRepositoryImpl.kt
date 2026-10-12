package com.manmadehorrorsstudio.gitscape.data.local

import com.manmadehorrorsstudio.gitscape.AppSettings
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage
import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage.Companion.fromLanguageCode
import com.manmadehorrorsstudio.gitscape.ui.models.ColorStyle
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SettingsRepositoryImpl  @Inject constructor(
    private val dao: SettingsDao,
    private val appSettings: AppSettings
): SettingsRepository {

    override suspend fun initDao() {
        if(dao.observeSettings().firstOrNull() == null)
            dao.saveSettings(
                SettingsEntity()
            )
    }

    override fun observeSettings(): Flow<SettingsEntity?> =
        dao.observeSettings()
            .onEach { settings ->
                settings?.let { sett ->
                    appSettings.setLanguageState(fromLanguageCode(sett.language))
                    appSettings.setRepoListEnabledState(sett.repoListEnabled)
                    appSettings.setColorStyleState(sett.colorStyle)
                }
            }

    override fun getLanguage(): StateFlow<AppLanguage> = appSettings.appLanguageState

    override suspend fun saveLanguage(language: AppLanguage) {
        gitScapeLog("Language ${language.name} selected")
        dao.observeSettings().firstOrNull()?.let { currentSettings ->
            dao.saveSettings(
                currentSettings.copy(language = language.locale.language)
            )
        }
    }

    override fun getRepoListEnabled(): StateFlow<Boolean> = appSettings.repoListEnabled

    override suspend fun saveRepoListEnabled(isEnabled: Boolean) {
        dao.observeSettings().firstOrNull()?.let { currentSettings ->
            dao.saveSettings(
                if (isEnabled)
                    currentSettings.copy(repoListEnabled = true)
                else
                    currentSettings.copy(repoListEnabled = false, repoListHash = null)
            )
        }
    }

     override suspend fun getRepoListHash(): String? {
         return dao.getSettings().repoListHash
    }

    override suspend fun saveRepoListHash(hash: String?) {
        dao.observeSettings().firstOrNull()?.let { currentSettings ->
            dao.saveSettings(
                currentSettings.copy(repoListHash = hash)
            )
        }
    }

    override suspend fun clearRepoList() {
        saveRepoListHash(null)
    }

    override fun getColorStyle(): StateFlow<ColorStyle> = appSettings.colorStyle

    override suspend fun saveColorStyle(colorStyle: ColorStyle) {
        dao.observeSettings().firstOrNull()?.let { currentSettings ->
            dao.saveSettings(
                currentSettings.copy(colorStyle = colorStyle)
            )
        }
    }
}