package com.manmadehorrorsstudio.gitscape.ui.viewmodels

import com.manmadehorrorsstudio.gitscape.TestUtils.mockGitScapeLog
import com.manmadehorrorsstudio.gitscape.TestUtils.unmockGitScapeLog
import com.manmadehorrorsstudio.gitscape.data.local.AuthCache
import com.manmadehorrorsstudio.gitscape.domain.models.AuthDeviceCode
import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalGitHubRepository
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalTokensRepository
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetAuthDeviceUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetAuthTokensUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubUserUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.RefreshAuthTokensUseCase
import com.manmadehorrorsstudio.gitscape.ui.models.AuthStatus
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class AuthViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var getDevice: GetAuthDeviceUseCase
    private lateinit var getTokens: GetAuthTokensUseCase
    private lateinit var getUser: GetGitHubUserUseCase
    private lateinit var refresh: RefreshAuthTokensUseCase
    private lateinit var local: LocalTokensRepository
    private lateinit var settings: SettingsRepository
    private lateinit var localGitHub: LocalGitHubRepository
    private lateinit var viewModel: AuthViewModel

    @BeforeEach fun setUp() {
        Dispatchers.setMain(dispatcher); mockGitScapeLog()
        getDevice = mockk(); getTokens = mockk(); getUser = mockk(); refresh = mockk()
        local = mockk(relaxed = true); settings = mockk(relaxed = true); localGitHub = mockk(relaxed = true)
        coEvery { local.getTokens() } returns null
        viewModel = AuthViewModel(getDevice, getTokens, getUser, refresh, local, settings, localGitHub, AuthCache())
    }
    @AfterEach fun tearDown() { unmockGitScapeLog(); Dispatchers.resetMain() }

    @Test fun `init with no local tokens resets loading and remains step one`() = runTest {
        advanceUntilIdle()
        assertEquals(AuthStatus.STEP1, viewModel.authState.value.authStatus)
        assertEquals(false, viewModel.authState.value.isLoading)
        coVerify { local.getTokens() }
    }

    @Test fun `step one loads device and step two opens browser`() = runTest {
        val device = AuthDeviceCode("d", "u", "url", System.currentTimeMillis() + 10_000, 1)
        coEvery { getDevice() } returns device
        advanceUntilIdle(); viewModel.authAdvanceStep(); advanceUntilIdle()
        assertEquals(AuthStatus.STEP2, viewModel.authState.value.authStatus)
        assertEquals(device, viewModel.authState.value.authDeviceCode)
        viewModel.authAdvanceStep(); advanceUntilIdle()
        assertEquals(AuthStatus.STEP1, viewModel.authState.value.authStatus)
    }

    @Test fun `successful token polling loads user and saves tokens`() = runTest {
        val device = AuthDeviceCode("d", "u", "url", System.currentTimeMillis() + 10_000, 0)
        val tokens = AuthTokens("", "a", "r", 1, 2)
        val user = GitHubUser("id", "name", null, null)
        coEvery { getDevice() } returns device; coEvery { getTokens("d", device.expiresAt, 0) } returns tokens
        coEvery { getUser() } returns user; coEvery { local.saveTokens(any()) } returns Unit
        advanceUntilIdle(); viewModel.authAdvanceStep(); advanceUntilIdle(); viewModel.authAdvanceStep(); advanceUntilIdle(); viewModel.authAdvanceStep(); advanceUntilIdle()
        assertEquals(AuthStatus.VALIDATED, viewModel.authState.value.authStatus)
        coVerify { local.saveTokens(AuthTokens("id", "a", "r", 1, 2)) }
    }
}
