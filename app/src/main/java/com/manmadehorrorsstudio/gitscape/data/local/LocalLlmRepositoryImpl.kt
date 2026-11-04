package com.manmadehorrorsstudio.gitscape.data.local

import com.manmadehorrorsstudio.gitscape.data.ai.LlmCache
import com.manmadehorrorsstudio.gitscape.data.mappers.mapToDomain
import com.manmadehorrorsstudio.gitscape.data.mappers.mapToEntity
import com.manmadehorrorsstudio.gitscape.domain.models.LlmConfiguration
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalLlmRepository
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class LocalLlmRepositoryImpl@Inject constructor(
    private val dao: LlmConfigurationDao,
    private val llmCache: LlmCache
): LocalLlmRepository {
    override suspend fun initDao() {
        val alreadyInDbConfigurationsId = dao.getLlmConfigurations().mapTo(hashSetOf()) {
            it.providerId
        }
        LlmConfiguration.getAllLlmConfigurations(
                aiKey = null,
                selectedModel = null,
                selected = false
        )
            .filter { configuration ->
                configuration.llmProvider.id !in alreadyInDbConfigurationsId
            }
            .forEach { configuration ->
                dao.saveLlmConfiguration(configuration.mapToEntity())
            }
    }

    override fun getCachedLlmConfigurations(): StateFlow<List<LlmConfiguration>> = llmCache.llmConfiguration

    override suspend fun observeLlmConfiguration(): Flow<List<LlmConfigurationEntity>> {
        return dao.observeLlmConfiguration().onEach { configurations ->
            gitScapeLog("observeLlmConfiguration: $configurations")
            configurations.mapToDomain().forEach { conf ->
                llmCache.updateLlmConfiguration(conf)
            }
        }
    }

    override suspend fun saveLlmConfiguration(llmConfiguration: LlmConfiguration) {
        dao.saveLlmConfiguration(llmConfiguration.mapToEntity())
    }
}



