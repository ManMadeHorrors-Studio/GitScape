package com.manmadehorrorsstudio.gitscape.data.mappers

import com.manmadehorrorsstudio.gitscape.data.local.LlmConfigurationEntity
import com.manmadehorrorsstudio.gitscape.domain.models.LlmConfiguration
import com.manmadehorrorsstudio.gitscape.domain.models.LlmConfiguration.Companion.getLlmConfiguration

fun LlmConfigurationEntity.mapToDomain(): LlmConfiguration? {
    return getLlmConfiguration(providerId, aiKey, selectedModelId, selected)
}

fun List<LlmConfigurationEntity>.mapToDomain(): List<LlmConfiguration> =
    mapNotNull { it.mapToDomain() }

fun LlmConfiguration.mapToEntity(): LlmConfigurationEntity =
    LlmConfigurationEntity(
        providerId = llmProvider.id,
        aiKey = aiKey,
        selectedModelId = selectedModel,
        selected = selected
    )