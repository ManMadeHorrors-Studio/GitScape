package com.manmadehorrorsstudio.gitscape.data.local

import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel
import com.manmadehorrorsstudio.gitscape.domain.models.LlmConfiguration
import com.manmadehorrorsstudio.gitscape.gitScapeLog


class LlmKoogModels() {

    companion object {
        private val openAiModels: List<LLModel> by lazy {
            listOf(
                OpenAIModels.Chat.GPT4o,
                OpenAIModels.Chat.GPT4oMini,
                OpenAIModels.Chat.GPT4_1,
                OpenAIModels.Chat.GPT4_1Nano,
                OpenAIModels.Chat.GPT4_1Mini,
                OpenAIModels.Chat.O1,
                OpenAIModels.Chat.O3,
                OpenAIModels.Chat.O3Mini,
                OpenAIModels.Chat.O4Mini,
                OpenAIModels.Chat.GPT5,
                OpenAIModels.Chat.GPT5Mini,
                OpenAIModels.Chat.GPT5Nano,
                OpenAIModels.Chat.GPT5Codex,
                OpenAIModels.Chat.GPT5Pro,
                OpenAIModels.Chat.GPT5_1,
                OpenAIModels.Chat.GPT5_1Codex,
                OpenAIModels.Chat.GPT5_1CodexMax,
                OpenAIModels.Chat.GPT5_2,
                OpenAIModels.Chat.GPT5_2Pro,
                OpenAIModels.Chat.GPT5_2Codex,
                OpenAIModels.Chat.GPT5_3Codex,
                OpenAIModels.Chat.GPT5_4,
                OpenAIModels.Chat.GPT5_4Mini,
                OpenAIModels.Chat.GPT5_4Nano,
                OpenAIModels.Chat.GPT5_4Pro,
                OpenAIModels.Chat.GPT5_5,
                OpenAIModels.Chat.GPT5_5Pro,
                OpenAIModels.Chat.GPT5_6Sol,
                OpenAIModels.Chat.GPT5_6Terra,
                OpenAIModels.Chat.GPT5_6Luna,
                // Non utilizzabili da AIAgent: modelli audio, embeddings e moderation.
                // OpenAIModels.Audio.GptAudio,
                // OpenAIModels.Audio.GPT4oMiniAudio,
                // OpenAIModels.Audio.GPT4oAudio,
                // OpenAIModels.Embeddings.TextEmbedding3Small,
                // OpenAIModels.Embeddings.TextEmbedding3Large,
                // OpenAIModels.Embeddings.TextEmbeddingAda002,
                // OpenAIModels.Moderation.Omni
            )
        }

        private val geminiModels: List<LLModel> by lazy {
            listOf(
                GoogleModels.Gemini2_0FlashLite001,
                GoogleModels.Gemini2_5Pro,
                GoogleModels.Gemini2_5Flash,
                GoogleModels.Gemini2_5FlashLite,
                GoogleModels.Gemini3_Flash_Preview,
                GoogleModels.Gemini3_1Pro_Preview,
                GoogleModels.Gemini3_1FlashLite_Preview,
                GoogleModels.Gemini3_1FlashLite,
                GoogleModels.Gemini3_5Flash,
                GoogleModels.Gemini3_5FlashLite,
                GoogleModels.Gemini3_6Flash,
                GoogleModels.Gemini3_7Flash,
                // Non utilizzabile da AIAgent: modello di embedding.
                // GoogleModels.Embeddings.GeminiEmbedding001
            )
        }

        fun getRequiredLlmKoogModel(llmConfiguration: LlmConfiguration?): LLModel? {
            val selectedModel = getAllLlmKoogModels(llmConfiguration?.llmProvider?.id).firstOrNull { model ->
                    model.id == llmConfiguration?.selectedModel
                }
            gitScapeLog("getRequiredLlmKoogModel modelId=${llmConfiguration?.selectedModel} selectedModel=$selectedModel")
            return selectedModel
        }

        fun getAllLlmKoogModels(llmProviderId: String?): List<LLModel> {
            return when (llmProviderId) {
                LLMProvider.Google.id -> geminiModels
                LLMProvider.OpenAI.id -> openAiModels
                else -> emptyList()
            }
        }
    }
}