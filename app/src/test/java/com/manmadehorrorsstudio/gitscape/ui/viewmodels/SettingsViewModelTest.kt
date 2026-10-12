package com.manmadehorrorsstudio.gitscape.ui.viewmodels

import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalGitHubRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var localGitHubRepository: LocalGitHubRepository

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        settingsRepository = mockk()
        localGitHubRepository = mockk(relaxed = true)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getLanguageState returns repository language state`() {
        val languageFlow = MutableStateFlow(AppLanguage.IT)

        every {
            settingsRepository.getLanguage()
        } returns languageFlow

        val viewModel = SettingsViewModel(settingsRepository, localGitHubRepository)

        assertEquals(
            languageFlow,
            viewModel.getLanguageState()
        )

        verify(exactly = 1) {
            settingsRepository.getLanguage()
        }
    }

    @Test
    fun `getGlowEffectOnScrollEnabledState returns repository glow state`() {
        val glowFlow = MutableStateFlow(false)

        every {
            settingsRepository.getGlowEffectOnScrollEnabled()
        } returns glowFlow

        val viewModel = SettingsViewModel(settingsRepository, localGitHubRepository)

        assertEquals(glowFlow, viewModel.getGlowEffectOnScrollEnabledState())

        verify(exactly = 1) {
            settingsRepository.getGlowEffectOnScrollEnabled()
        }
    }

    @Test
    fun `changeGlowEffectOnScrollEnabled saves selected value`() = runTest {
        coEvery {
            settingsRepository.saveGlowEffectOnScrollEnabled(false)
        } just Runs

        val viewModel = SettingsViewModel(settingsRepository, localGitHubRepository)

        viewModel.changeGlowEffectOnScrollEnabled(false)

        advanceUntilIdle()

        coVerify(exactly = 1) {
            settingsRepository.saveGlowEffectOnScrollEnabled(false)
        }
    }

    @Test
    fun `changeLanguage saves selected language`() = runTest {

        coEvery {
            settingsRepository.saveLanguage(AppLanguage.EN)
        } just Runs

        val viewModel = SettingsViewModel(settingsRepository, localGitHubRepository)

        viewModel.changeLanguage(AppLanguage.EN)

        advanceUntilIdle()

        coVerify(exactly = 1) {
            settingsRepository.saveLanguage(AppLanguage.EN)
        }
    }
}