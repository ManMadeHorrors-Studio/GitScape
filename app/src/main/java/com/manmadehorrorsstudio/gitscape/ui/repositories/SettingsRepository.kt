package com.manmadehorrorsstudio.gitscape.ui.repositories

import com.manmadehorrorsstudio.gitscape.data.local.SettingsEntity
import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    suspend fun initDao()
    fun observeSettings(): Flow<SettingsEntity?>

    fun getLanguage(): StateFlow<AppLanguage>
    suspend fun saveLanguage(language: AppLanguage)

    suspend fun getRepoListSave(): Boolean
    suspend fun saveRepoListSave(toSave: Boolean)
}