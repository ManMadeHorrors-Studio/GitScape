package com.manmadehorrorsstudio.gitscape.data.mappers

import com.manmadehorrorsstudio.gitscape.data.local.LlmConfigurationEntity
import com.manmadehorrorsstudio.gitscape.domain.models.LlmConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LlmConfigurationMapperTest {
    @Test
    fun `domain maps to entity preserving provider key and selected values`() {
        val configuration = LlmConfiguration.OpenAiLlmConfiguration(
            aiKey = "test-key",
            selectedModel = "gpt-4.1-mini",
            selected = true
        )

        val entity = configuration.mapToEntity()

        assertEquals("openai", entity.providerId)
        assertEquals("test-key", entity.aiKey)
        assertEquals("gpt-4.1-mini", entity.selectedModelId)
        assertTrue(entity.selected)
    }

    @Test
    fun `entity maps to domain preserving provider key model and selection`() {
        val entity = LlmConfigurationEntity(
            providerId = "google",
            aiKey = "google-key",
            selectedModelId = "gemini-3.7-flash",
            selected = true
        )

        val configuration = entity.mapToDomain()

        assertEquals(
            LlmConfiguration.GeminiLlmConfiguration(
                aiKey = "google-key",
                selectedModel = "gemini-3.7-flash",
                selected = true
            ),
            configuration
        )
    }

    @Test
    fun `unknown provider entity is ignored when mapping a list`() {
        val result = listOf(
            LlmConfigurationEntity(providerId = "unknown"),
            LlmConfigurationEntity(providerId = "openai")
        ).mapToDomain()

        assertEquals(1, result.size)
        assertEquals("openai", result.single().llmProvider.id)
    }
}
