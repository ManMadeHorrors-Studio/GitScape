package com.manmadehorrorsstudio.gitscape.data.local

import com.manmadehorrorsstudio.gitscape.AppSettings
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage
import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage.Companion.fromLanguageCode
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
                }
            }

    override fun getLanguage(): StateFlow<AppLanguage> = appSettings.appLanguageState

    override suspend fun saveLanguage(language: AppLanguage) {
        gitScapeLog("Language ${language.name} selected")
        val currentSettings = dao.observeSettings().firstOrNull()
        dao.saveSettings(
            currentSettings?.copy(language = language.locale.language) ?: SettingsEntity(language = language.locale.language)
        )
    }
}