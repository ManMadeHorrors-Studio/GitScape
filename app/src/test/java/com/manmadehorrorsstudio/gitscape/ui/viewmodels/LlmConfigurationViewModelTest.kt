package com.manmadehorrorsstudio.gitscape.ui.viewmodels

import com.manmadehorrorsstudio.gitscape.domain.models.LlmConfiguration
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalLlmRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LlmConfigurationViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: LocalLlmRepository
    private lateinit var configurations: MutableStateFlow<List<LlmConfiguration>>
    private lateinit var viewModel: LlmConfigurationViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = mockk()
        configurations = MutableStateFlow(
            listOf(
                LlmConfiguration.OpenAiLlmConfiguration(
                    aiKey = "openai-key",
                    selectedModel = "gpt-4.1-mini",
                    selected = true
                ),
                LlmConfiguration.GeminiLlmConfiguration(
                    aiKey = null,
                    selectedModel = null,
                    selected = false
                )
            )
        )
        every { repository.getCachedLlmConfigurations() } returns configurations
        coEvery { repository.saveLlmConfiguration(any()) } just Runs
        coEvery { repository.clearLlmConfigurations() } just Runs
        viewModel = LlmConfigurationViewModel(repository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `provider selection exposes matching configuration`() = runTest {
        backgroundScope.launch {
            viewModel.selectedLlmConfigurationState.collect()
        }
        viewModel.setLlmInConfiguration("openai")
        advanceUntilIdle()

        assertEquals(
            configurations.value.first(),
            viewModel.selectedLlmConfigurationState.value
        )
    }

    @Test
    fun `models put selected model first`() = runTest {
        backgroundScope.launch {
            viewModel.selectedLlmConfigurationState.collect()
        }
        viewModel.setLlmInConfiguration("openai")
        advanceUntilIdle()

        val models = viewModel.getModelsForConfiguration()

        assertEquals("gpt-4.1-mini", models.first().id)
        assertTrue(models.size > 1)
    }

    @Test
    fun `saveAiKey saves selected provider with updated key`() = runTest {
        backgroundScope.launch {
            viewModel.selectedLlmConfigurationState.collect()
        }
        viewModel.setLlmInConfiguration("openai")
        advanceUntilIdle()

        viewModel.saveAiKey("new-key")
        advanceUntilIdle()

        coVerify {
            repository.saveLlmConfiguration(
                LlmConfiguration.OpenAiLlmConfiguration(
                    aiKey = "new-key",
                    selectedModel = "gpt-4.1-mini",
                    selected = true
                )
            )
        }
    }

    @Test
    fun `saveModel saves selected model for the provider and deselects other configurations`() = runTest {
        backgroundScope.launch {
            viewModel.selectedLlmConfigurationState.collect()
        }
        viewModel.setLlmInConfiguration("openai")
        advanceUntilIdle()

        viewModel.saveModel("gpt-4o")
        advanceUntilIdle()

        coVerify {
            repository.saveLlmConfiguration(
                LlmConfiguration.OpenAiLlmConfiguration(
                    aiKey = "openai-key",
                    selectedModel = "gpt-4o",
                    selected = true
                )
            )
            repository.saveLlmConfiguration(
                LlmConfiguration.GeminiLlmConfiguration(
                    aiKey = null,
                    selectedModel = null,
                    selected = false
                )
            )
        }
    }
}
