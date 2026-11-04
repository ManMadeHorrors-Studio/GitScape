package com.manmadehorrorsstudio.gitscape.domain.models

import ai.koog.prompt.llm.LLMProvider

sealed class LlmConfiguration(
    val llmProvider: LLMProvider,
    open val aiKey: String?,
    open val selectedModel: String?,
    open val selected: Boolean = false
) {
    data class OpenAiLlmConfiguration(
        override val aiKey: String? = null,
        override val selectedModel: String? = null,
        override val selected: Boolean = false
    ) : LlmConfiguration(
        llmProvider = LLMProvider.OpenAI,
        aiKey = aiKey,
        selectedModel = selectedModel,
        selected = selected
    )

    data class GeminiLlmConfiguration(
        override val aiKey: String? = null,
        override val selectedModel: String? = null,
        override val selected: Boolean = false
    ) : LlmConfiguration(
        llmProvider = LLMProvider.Google,
        aiKey = aiKey,
        selectedModel = selectedModel,
        selected = selected
    )

    companion object {
        fun getAllLlmConfigurations(
            aiKey: String?,
            selectedModel: String?,
            selected: Boolean
        ): List<LlmConfiguration> = listOf(
            OpenAiLlmConfiguration(aiKey, selectedModel, selected),
            GeminiLlmConfiguration(aiKey, selectedModel, selected)
        )

        fun getLlmConfiguration(
            providerId: String,
            aiKey: String?,
            selectedModel: String?,
            selected: Boolean = false
        ): LlmConfiguration? =
            getAllLlmConfigurations(aiKey, selectedModel, selected).firstOrNull { conf ->
                conf.llmProvider.id == providerId
            }
    }
}
