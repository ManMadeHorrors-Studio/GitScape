package com.manmadehorrorsstudio.gitscape.ui.repositories

import com.manmadehorrorsstudio.gitscape.data.local.SettingsEntity
import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage
import com.manmadehorrorsstudio.gitscape.ui.models.ColorStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    suspend fun initDao()
    fun observeSettings(): Flow<SettingsEntity?>

    fun getLanguage(): StateFlow<AppLanguage>
    suspend fun saveLanguage(language: AppLanguage)

    fun getRepoListEnabled(): StateFlow<Boolean>
    suspend fun saveRepoListEnabled(isEnabled: Boolean)

    suspend fun getRepoListHash(): String?
    suspend fun saveRepoListHash(hash: String?)

    suspend fun clearRepoList()

    fun getColorStyle(): StateFlow<ColorStyle>
    suspend fun saveColorStyle(colorStyle: ColorStyle)
}