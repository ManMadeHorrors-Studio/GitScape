package com.manmadehorrorsstudio.gitscape.data.local

import com.manmadehorrorsstudio.gitscape.data.ai.LlmCache
import com.manmadehorrorsstudio.gitscape.data.mappers.mapToEntity
import com.manmadehorrorsstudio.gitscape.domain.models.LlmConfiguration
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalLlmRepositoryImplTest {
    private lateinit var dao: LlmConfigurationDao
    private lateinit var cache: LlmCache
    private lateinit var repository: LocalLlmRepositoryImpl

    @BeforeEach
    fun setUp() {
        dao = mockk()
        cache = mockk(relaxed = true)
        repository = LocalLlmRepositoryImpl(dao, cache)
    }

    @Test
    fun `initDao adds only missing default providers`() = runTest {
        coEvery { dao.getLlmConfigurations() } returns listOf(
            LlmConfigurationEntity(providerId = "openai")
        )
        coEvery { dao.saveLlmConfiguration(any()) } just Runs

        repository.initDao()

        coVerify(exactly = 1) {
            dao.saveLlmConfiguration(
                LlmConfigurationEntity(providerId = "google")
            )
        }
    }

    @Test
    fun `initDao does not add defaults when all providers exist`() = runTest {
        coEvery { dao.getLlmConfigurations() } returns listOf(
            LlmConfigurationEntity(providerId = "openai"),
            LlmConfigurationEntity(providerId = "google")
        )

        repository.initDao()

        coVerify(exactly = 0) { dao.saveLlmConfiguration(any()) }
    }

    @Test
    fun `observeLlmConfiguration updates cache with every valid configuration`() = runTest {
        val entities = listOf(
            LlmConfigurationEntity(providerId = "openai", selected = true),
            LlmConfigurationEntity(providerId = "google", selected = false)
        )
        every { dao.observeLlmConfiguration() } returns flowOf(entities)

        repository.observeLlmConfiguration().first()

        coVerify(exactly = 2) { cache.updateLlmConfiguration(any()) }
    }

    @Test
    fun `saveLlmConfiguration maps and saves one entity`() = runTest {
        coEvery { dao.saveLlmConfiguration(any()) } just Runs
        val configuration = LlmConfiguration.OpenAiLlmConfiguration(
            aiKey = "key",
            selectedModel = "gpt-4.1-mini",
            selected = true
        )

        repository.saveLlmConfiguration(configuration)

        coVerify {
            dao.saveLlmConfiguration(configuration.mapToEntity())
        }
    }

    @Test
    fun `clearLlmConfigurations clears and reinitializes defaults`() = runTest {
        coEvery { dao.clearLlmConfiguration() } just Runs
        coEvery { dao.getLlmConfigurations() } returns emptyList()
        coEvery { dao.saveLlmConfiguration(any()) } just Runs

        repository.clearLlmConfigurations()

        coVerify(exactly = 1) { dao.clearLlmConfiguration() }
        coVerify(exactly = 2) { dao.saveLlmConfiguration(any()) }
    }
}
