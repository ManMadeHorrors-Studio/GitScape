package com.manmadehorrorsstudio.gitscape.ui.repositories

import com.manmadehorrorsstudio.gitscape.AppLanguage
import com.manmadehorrorsstudio.gitscape.data.local.SettingsEntity
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun initDao()
    fun observeSettings(): Flow<SettingsEntity?>
    suspend fun saveLanguage(language: AppLanguage)
}