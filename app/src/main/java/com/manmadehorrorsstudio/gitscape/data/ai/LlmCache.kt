package com.manmadehorrorsstudio.gitscape.data.ai

import ai.koog.http.client.ktor.KtorKoogHttpClient
import ai.koog.prompt.executor.clients.google.GoogleLLMClient
import ai.koog.prompt.executor.clients.openai.OpenAILLMClient
import ai.koog.prompt.executor.llms.MultiLLMPromptExecutor
import ai.koog.prompt.llm.LLMProvider
import com.manmadehorrorsstudio.gitscape.domain.models.LlmConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.filterNot
import kotlin.collections.firstOrNull
import kotlin.collections.plus

@Singleton
class LlmCache @Inject constructor() {

    private val _llmConfiguration = MutableStateFlow<List<LlmConfiguration>>(emptyList())
    val llmConfiguration = _llmConfiguration.asStateFlow()

    @Volatile
    private var llmExecutor: MultiLLMPromptExecutor? = null

    fun getLlmExecutor(): MultiLLMPromptExecutor? = llmExecutor

    fun getSelectedLlmConfiguration(): LlmConfiguration? = _llmConfiguration.value.firstOrNull{it.selected}

    fun updateLlmConfiguration(configuration: LlmConfiguration) {
        val configurations = _llmConfiguration.value
            .filterNot { conf -> conf.llmProvider.id == configuration.llmProvider.id } + configuration
        _llmConfiguration.value = configurations

        updateLlmExecutor()
    }

    fun clearCache() {
        _llmConfiguration.value = emptyList()
        llmExecutor = null
    }

    private fun updateLlmExecutor() {
        val selectedLlmConfiguration = getSelectedLlmConfiguration()
        llmExecutor = selectedLlmConfiguration?.aiKey?.let { aiKey ->
             when (selectedLlmConfiguration.llmProvider) {
                LLMProvider.OpenAI -> MultiLLMPromptExecutor(
                    OpenAILLMClient(
                        apiKey = aiKey,
                        httpClientFactory = KtorKoogHttpClient.Factory()
                    )
                )
                LLMProvider.Google -> MultiLLMPromptExecutor(
                    GoogleLLMClient(
                        apiKey = aiKey,
                        httpClientFactory = KtorKoogHttpClient.Factory()
                    )
                )
                else -> null
            }
        }
    }
}