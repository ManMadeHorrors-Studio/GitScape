package com.manmadehorrorsstudio.gitscape.ui.viewmodels

import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
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

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        settingsRepository = mockk()
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

        val viewModel = SettingsViewModel(settingsRepository)

        assertEquals(
            languageFlow,
            viewModel.getLanguageState()
        )

        verify(exactly = 1) {
            settingsRepository.getLanguage()
        }
    }

    @Test
    fun `changeLanguage saves selected language`() = runTest {

        coEvery {
            settingsRepository.saveLanguage(AppLanguage.EN)
        } just Runs

        val viewModel = SettingsViewModel(settingsRepository)

        viewModel.changeLanguage(AppLanguage.EN)

        advanceUntilIdle()

        coVerify(exactly = 1) {
            settingsRepository.saveLanguage(AppLanguage.EN)
        }
    }
}