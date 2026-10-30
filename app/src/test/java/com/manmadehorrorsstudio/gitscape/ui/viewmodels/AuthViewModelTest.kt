package com.manmadehorrorsstudio.gitscape.ui.viewmodels

import com.manmadehorrorsstudio.gitscape.ACCESSTOKEN_SAFETY_TIME
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.REFRESHTOKEN_SAFETY_TIME
import com.manmadehorrorsstudio.gitscape.addCurrentTime
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
import com.manmadehorrorsstudio.gitscape.ui.models.AuthError
import com.manmadehorrorsstudio.gitscape.ui.models.AuthStatus
import com.manmadehorrorsstudio.gitscape.ui.models.UIEvents
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
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
    private lateinit var authCache: AuthCache

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockGitScapeLog()
        getDevice = mockk()
        getTokens = mockk()
        getUser = mockk()
        refresh = mockk()
        local = mockk(relaxed = true)
        settings = mockk(relaxed = true)
        localGitHub = mockk(relaxed = true)
        authCache = AuthCache()
        coEvery { local.getTokens() } returns null
    }

    @AfterEach
    fun tearDown() {
        unmockGitScapeLog()
        Dispatchers.resetMain()
    }

    private fun createViewModel() = AuthViewModel(
        getDevice,
        getTokens,
        getUser,
        refresh,
        local,
        settings,
        localGitHub,
        authCache
    )

    @Test
    fun `init without local tokens clears local data and finishes loading`() = runTest {
        val vm = createViewModel()
        advanceUntilIdle()

        assertEquals(AuthStatus.STEP1, vm.authState.value.authStatus)
        assertFalse(vm.authState.value.isLoading)
        coVerify { local.getTokens() }
        coVerify { local.deleteTokens() }
        coVerify { localGitHub.clearGitHubRepositories() }
        coVerify { settings.clearRepoHash() }
    }

    @Test
    fun `valid local access token validates matching user and requests repository refresh`() = runTest {
        val tokens = AuthTokens(
            "user-id",
            "access",
            "refresh",
            60L.addCurrentTime(ACCESSTOKEN_SAFETY_TIME)!!,
            60L.addCurrentTime(REFRESHTOKEN_SAFETY_TIME)!!
        )
        val user = GitHubUser("user-id", "name", null, null)
        coEvery { local.getTokens() } returns tokens
        coEvery { getUser() } returns user

        val vm = createViewModel()
        advanceUntilIdle()

        assertEquals(AuthStatus.VALIDATED, vm.authState.value.authStatus)
        assertEquals(user, vm.authState.value.gitHubUser)
        assertEquals("access", authCache.getAccessToken())
        assertTrue(vm.authEvents.first() is UIEvents.CallRefreshRepo)
    }

    @Test
    fun `local token belonging to another user resets authentication and emits error`() = runTest {
        val tokens = AuthTokens(
            "different-id",
            "access",
            "refresh",
            60L.addCurrentTime(ACCESSTOKEN_SAFETY_TIME)!!,
            60L.addCurrentTime(REFRESHTOKEN_SAFETY_TIME)!!
        )
        coEvery { local.getTokens() } returns tokens
        coEvery { getUser() } returns GitHubUser("actual-id", "name", null, null)

        val vm = createViewModel()
        advanceUntilIdle()
        val event = vm.authEvents.first()
        advanceUntilIdle()

        assertEquals(AuthStatus.STEP1, vm.authState.value.authStatus)
        assertTrue(event is UIEvents.ShowError)
        assertEquals(AuthError.DIFFERENT_USER.message, (event as UIEvents.ShowError).message)
        assertEquals(null, authCache.getAccessToken())
    }

    @Test
    fun `expired access token refreshes tokens and saves them with the current user`() = runTest {
        val oldTokens = AuthTokens(
            "old-id",
            "expired-access",
            "refresh",
            0,
            60L.addCurrentTime(REFRESHTOKEN_SAFETY_TIME)
        )
        val refreshed = AuthTokens(
            "",
            "new-access",
            "new-refresh",
            60L.addCurrentTime(ACCESSTOKEN_SAFETY_TIME),
            120L.addCurrentTime(REFRESHTOKEN_SAFETY_TIME)!!
        )
        val user = GitHubUser("user-id", "name", null, null)
        coEvery { local.getTokens() } returns oldTokens
        coEvery { refresh("refresh") } returns refreshed
        coEvery { getUser() } returns user

        val vm = createViewModel()
        advanceUntilIdle()

        assertEquals(AuthStatus.SUCCESS, vm.authState.value.authStatus)
        coVerify { refresh("refresh") }
        coVerify {
            local.saveTokens(
                match {
                    it.githubUserId == "user-id" &&
                        it.accessToken == "new-access" &&
                        it.refreshToken == "new-refresh"
                }
            )
        }
    }

    @Test
    fun `device authentication opens browser and polls while foreground`() = runTest {
        val device = AuthDeviceCode("device", "user-code", "https://github.com/login/device", Long.MAX_VALUE, 0)
        val tokens = AuthTokens("", "access", "refresh", 1, 2)
        val user = GitHubUser("user-id", "name", null, null)
        coEvery { getDevice() } returns device
        coEvery { getTokens("device", device.expiresAt, 0) } returns tokens
        coEvery { getUser() } returns user

        val vm = createViewModel()
        advanceUntilIdle()
        vm.authAdvanceStep()
        advanceUntilIdle()

        assertEquals(AuthStatus.STEP2, vm.authState.value.authStatus)
        vm.authAdvanceStep()
        val browserEvent = vm.authEvents.first()
        assertEquals(AuthStatus.BROWSING, vm.authState.value.authStatus)
        assertEquals(UIEvents.CallOpenBrowser(device.verificationUri, device.userCode), browserEvent)

        advanceUntilIdle()
        assertEquals(AuthStatus.SUCCESS, vm.authState.value.authStatus)
        coVerify { getTokens("device", device.expiresAt, 0) }
    }

    @Test
    fun `auth token failure resets state and emits numeric error`() = runTest {
        val device = AuthDeviceCode("device", "user-code", "url", Long.MAX_VALUE, 0)
        coEvery { getDevice() } returns device
        coEvery { getTokens(any(), any(), any()) } throws IllegalStateException(R.string.AUTH_ERROR_DENIED.toString())

        val vm = createViewModel()
        advanceUntilIdle()
        vm.authAdvanceStep()
        advanceUntilIdle()
        vm.authAdvanceStep()
        advanceUntilIdle()

        val event = vm.authEvents.first()
        assertTrue(event is UIEvents.ShowError)
        assertEquals(R.string.AUTH_ERROR_DENIED, (event as UIEvents.ShowError).message)
        assertEquals(AuthStatus.STEP1, vm.authState.value.authStatus)
    }

    @Test
    fun `expired device code resets state and emits timeout`() = runTest {
        val expiredDevice = AuthDeviceCode("device", "user-code", "url", 0, 0)
        coEvery { getDevice() } returns expiredDevice

        val vm = createViewModel()
        advanceUntilIdle()
        vm.authAdvanceStep()
        advanceUntilIdle()
        vm.authAdvanceStep()
        advanceUntilIdle()

        val event = vm.authEvents.first()
        assertTrue(event is UIEvents.ShowError)
        assertEquals(R.string.AUTH_WELCOME_TIMEOUT, (event as UIEvents.ShowError).message)
        assertEquals(AuthStatus.STEP1, vm.authState.value.authStatus)
        coVerify(exactly = 0) { getTokens(any(), any(), any()) }
    }

    @Test
    fun `foreground changes cancel polling and cancel action returns to step one`() = runTest {
        val device = AuthDeviceCode("device", "user-code", "url", Long.MAX_VALUE, 0)
        coEvery { getDevice() } returns device
        coEvery { getTokens(any(), any(), any()) } coAnswers {
            kotlinx.coroutines.awaitCancellation()
        }

        val vm = createViewModel()
        advanceUntilIdle()
        vm.authAdvanceStep()
        advanceUntilIdle()
        vm.authAdvanceStep()
        advanceUntilIdle()
        assertEquals(AuthStatus.BROWSING, vm.authState.value.authStatus)

        vm.onForegroundChanged(false)
        advanceUntilIdle()
        vm.authAdvanceStep()
        advanceUntilIdle()

        assertEquals(AuthStatus.STEP1, vm.authState.value.authStatus)
        assertEquals(null, vm.authState.value.authDeviceCode)
        coVerify(exactly = 0) { local.saveTokens(any()) }
    }

    @Test
    fun `success advances to validated and emits repository refresh`() = runTest {
        val device = AuthDeviceCode("device", "user-code", "url", Long.MAX_VALUE, 0)
        val tokens = AuthTokens("", "access", "refresh", 1, 2)
        coEvery { getDevice() } returns device
        coEvery { getTokens(any(), any(), any()) } returns tokens
        coEvery { getUser() } returns GitHubUser("id", "name", null, null)

        val vm = createViewModel()
        advanceUntilIdle()
        vm.authAdvanceStep()
        advanceUntilIdle()
        vm.authAdvanceStep()
        advanceUntilIdle()

        assertEquals(AuthStatus.SUCCESS, vm.authState.value.authStatus)
        vm.authAdvanceStep()
        assertEquals(AuthStatus.VALIDATED, vm.authState.value.authStatus)
        assertTrue(vm.authEvents.first { it is UIEvents.CallRefreshRepo } is UIEvents.CallRefreshRepo)
    }
}
