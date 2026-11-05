package com.manmadehorrorsstudio.gitscape.domain.repositories

import com.manmadehorrorsstudio.gitscape.data.local.LlmConfigurationEntity
import com.manmadehorrorsstudio.gitscape.domain.models.LlmConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LocalLlmRepository {
    fun getCachedLlmConfigurations(): StateFlow<List<LlmConfiguration>>
    suspend fun initDao()
    suspend fun observeLlmConfiguration(): Flow<List<LlmConfigurationEntity>>
    suspend fun saveLlmConfiguration(llmConfiguration: LlmConfiguration)
    suspend fun clearLlmConfigurations()
}

