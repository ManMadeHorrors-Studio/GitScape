package com.manmadehorrorsstudio.gitscape.data.local

import com.manmadehorrorsstudio.gitscape.AppSettings
import com.manmadehorrorsstudio.gitscape.TestUtils.mockGitScapeLog
import com.manmadehorrorsstudio.gitscape.TestUtils.unmockGitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryImplTest {

    private lateinit var dao: SettingsDao
    private lateinit var appSettings: AppSettings

    private lateinit var repository: SettingsRepositoryImpl

    @BeforeEach
    fun setUp() {
        dao = mockk()
        appSettings = mockk()

        mockGitScapeLog()

        repository = SettingsRepositoryImpl(
            dao = dao,
            appSettings = appSettings
        )
    }

    @AfterEach
    fun tearDown() {
        unmockGitScapeLog()
    }

    @Test
    fun `initDao creates default settings when database is empty`() = runTest {

        every {
            dao.observeSettings()
        } returns flowOf(null)

        coEvery {
            dao.saveSettings(any())
        } just Runs

        repository.initDao()

        coVerify(exactly = 1) {
            dao.saveSettings(
                SettingsEntity()
            )
        }
    }

    @Test
    fun `initDao does not create settings when database is already initialized`() = runTest {

        val settings = SettingsEntity(
            language = AppLanguage.EN.locale.language
        )

        every {
            dao.observeSettings()
        } returns flowOf(settings)

        repository.initDao()

        coVerify(exactly = 0) {
            dao.saveSettings(any())
        }
    }

    @Test
    fun `observeSettings updates appSettings language`() = runTest {

        val settings = SettingsEntity(
            language = AppLanguage.EN.locale.language
        )

        every {
            dao.observeSettings()
        } returns flowOf(settings)

        every {
            appSettings.setLanguageState(any())
        } just Runs

        repository
            .observeSettings()
            .first()

        verify(exactly = 1) {
            appSettings.setLanguageState(
                AppLanguage.EN
            )
        }
    }

    @Test
    fun `observeSettings does not update appSettings when settings is null`() = runTest {

        every {
            dao.observeSettings()
        } returns flowOf(null)

        repository
            .observeSettings()
            .first()

        verify(exactly = 0) {
            appSettings.setLanguageState(any())
        }
    }

    @Test
    fun `getLanguage returns appSettings language state`() {

        val languageState = MutableStateFlow(AppLanguage.IT)

        every {
            appSettings.appLanguageState
        } returns languageState

        val result = repository.getLanguage()

        assertEquals(
            languageState,
            result
        )

        verify(exactly = 1) {
            appSettings.appLanguageState
        }
    }

    @Test
    fun `saveLanguage updates existing settings`() = runTest {

        val currentSettings = SettingsEntity(
            language = AppLanguage.IT.locale.language
        )

        every {
            dao.observeSettings()
        } returns flowOf(currentSettings)

        coEvery {
            dao.saveSettings(any())
        } just Runs

        repository.saveLanguage(AppLanguage.EN)

        coVerify(exactly = 1) {
            dao.saveSettings(
                currentSettings.copy(
                    language = AppLanguage.EN.locale.language
                )
            )
        }
    }

    @Test
    fun `saveLanguage creates settings when none exist`() = runTest {

        every {
            dao.observeSettings()
        } returns flowOf(null)

        coEvery {
            dao.saveSettings(any())
        } just Runs

        repository.saveLanguage(AppLanguage.EN)

        coVerify(exactly = 1) {
            dao.saveSettings(
                SettingsEntity(
                    language = AppLanguage.EN.locale.language
                )
            )
        }
    }
}