package com.manmadehorrorsstudio.gitscape.data.local

import com.manmadehorrorsstudio.gitscape.AppSettings
import com.manmadehorrorsstudio.gitscape.TestUtils.mockGitScapeLog
import com.manmadehorrorsstudio.gitscape.TestUtils.unmockGitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage
import com.manmadehorrorsstudio.gitscape.ui.models.ThemeStyle
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
        every {
            appSettings.setRepoListEnabledState(any())
        } just Runs
        every {
            appSettings.setGlowEffectOnScrollEnabledState(any())
        } just Runs
        every {
            appSettings.setThemeStyleState(any())
        } just Runs
        every {
            appSettings.setColorStyleState(any())
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
    fun `observeSettings updates appSettings glow effect state`() = runTest {
        val settings = SettingsEntity(glowEffectOnScrollEnabled = false)

        every { dao.observeSettings() } returns flowOf(settings)
        every { appSettings.setLanguageState(any()) } just Runs
        every { appSettings.setRepoListEnabledState(any()) } just Runs
        every { appSettings.setGlowEffectOnScrollEnabledState(any()) } just Runs
        every { appSettings.setThemeStyleState(any()) } just Runs
        every { appSettings.setColorStyleState(any()) } just Runs

        repository.observeSettings().first()

        verify(exactly = 1) {
            appSettings.setGlowEffectOnScrollEnabledState(false)
        }
    }

    @Test
    fun `observeSettings updates appSettings theme style`() = runTest {
        val settings = SettingsEntity(themeStyle = ThemeStyle.DARK)

        every { dao.observeSettings() } returns flowOf(settings)
        every { appSettings.setLanguageState(any()) } just Runs
        every { appSettings.setRepoListEnabledState(any()) } just Runs
        every { appSettings.setThemeStyleState(any()) } just Runs
        every { appSettings.setColorStyleState(any()) } just Runs
        every { appSettings.setGlowEffectOnScrollEnabledState(any()) } just Runs

        repository.observeSettings().first()

        verify(exactly = 1) {
            appSettings.setThemeStyleState(ThemeStyle.DARK)
        }
    }

    @Test
    fun `getThemeStyle returns appSettings theme state`() {
        val themeState = MutableStateFlow(ThemeStyle.LIGHT)
        every { appSettings.themeStyle } returns themeState

        assertEquals(themeState, repository.getThemeStyle())

        verify(exactly = 1) {
            appSettings.themeStyle
        }
    }

    @Test
    fun `saveThemeStyle updates existing settings`() = runTest {
        val currentSettings = SettingsEntity(themeStyle = ThemeStyle.DEFAULT)

        every { dao.observeSettings() } returns flowOf(currentSettings)
        coEvery { dao.saveSettings(any()) } just Runs

        repository.saveThemeStyle(ThemeStyle.DARK)

        coVerify(exactly = 1) {
            dao.saveSettings(currentSettings.copy(themeStyle = ThemeStyle.DARK))
        }
    }

    @Test
    fun `getGlowEffectEnabled returns appSettings glow effect state`() {
        val glowEffectState = MutableStateFlow(false)
        every { appSettings.glowEffectOnScrollEnabled } returns glowEffectState

        assertEquals(glowEffectState, repository.getGlowEffectOnScrollEnabled())

        verify(exactly = 1) {
            appSettings.glowEffectOnScrollEnabled
        }
    }

    @Test
    fun `saveGlowEffectEnabled updates existing settings`() = runTest {
        val currentSettings = SettingsEntity(glowEffectOnScrollEnabled = true)

        every { dao.observeSettings() } returns flowOf(currentSettings)
        coEvery { dao.saveSettings(any()) } just Runs

        repository.saveGlowEffectOnScrollEnabled(false)

        coVerify(exactly = 1) {
            dao.saveSettings(currentSettings.copy(glowEffectOnScrollEnabled = false))
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

}