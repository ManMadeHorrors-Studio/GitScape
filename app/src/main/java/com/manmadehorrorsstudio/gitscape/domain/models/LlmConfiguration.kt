package com.manmadehorrorsstudio.gitscape.domain.models

import ai.koog.prompt.llm.GoogleLLMProvider
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.llm.OpenAILLMProvider

sealed class LlmConfiguration(
    val llmProvider: LLMProvider,
    open val aiKey: String?,
    open val selectedModel: LLModel?,
    open var favouriteList: Set<String>
) {
    data class OpenAiLlmConfiguration(
        override val aiKey: String? = null,
        override val selectedModel: LLModel? = null,
        override var favouriteList:  Set<String> = emptySet()
    ) : LlmConfiguration(
        llmProvider = OpenAILLMProvider,
        aiKey = aiKey,
        selectedModel = selectedModel,
        favouriteList =  favouriteList
    )

    data class GeminiLlmConfiguration(
        override val aiKey: String? = null,
        override val selectedModel: LLModel? = null,
        override var favouriteList:  Set<String> = emptySet()
    ) : LlmConfiguration(
        llmProvider = GoogleLLMProvider,
        aiKey = aiKey,
        selectedModel = selectedModel,
        favouriteList = favouriteList
    )
}
