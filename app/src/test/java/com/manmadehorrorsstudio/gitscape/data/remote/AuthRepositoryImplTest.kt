package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.TestUtils.mockGitScapeLog
import com.manmadehorrorsstudio.gitscape.TestUtils.unmockGitScapeLog
import com.manmadehorrorsstudio.gitscape.data.models.AuthDeviceCodeDTO
import com.manmadehorrorsstudio.gitscape.data.models.AuthTokenDTO
import com.manmadehorrorsstudio.gitscape.ui.models.AuthError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthRepositoryImplTest {
    private lateinit var api: AuthApis
    private lateinit var repository: AuthRepositoryImpl

    @BeforeEach fun setUp() { mockGitScapeLog(); api = mockk(); repository = AuthRepositoryImpl(api) }
    @AfterEach fun tearDown() = unmockGitScapeLog()

    @Test fun `request device maps valid response`() = runTest {
        coEvery { api.requestDeviceCode() } returns AuthDeviceCodeDTO("d", "u", "url", 60, 1)
        assertEquals("d", repository.requestAuthDeviceCode().deviceCode)
    }

    @Test fun `request device rejects invalid response`() = runTest {
        coEvery { api.requestDeviceCode() } returns AuthDeviceCodeDTO()
        assertThrows(Exception::class.java) { runTest { repository.requestAuthDeviceCode() } }
    }

    @Test fun `refresh maps valid and rejects invalid`() = runTest {
        coEvery { api.refreshAccessToken("r") } returns AuthTokenDTO("a", "rt", 1, 2)
        assertEquals("a", repository.refreshAccessToken("r").accessToken)
        coEvery { api.refreshAccessToken("bad") } returns AuthTokenDTO()
        assertThrows(Exception::class.java) { runTest { repository.refreshAccessToken("bad") } }
    }

    @Test fun `poll pending then succeeds`() = runTest {
        coEvery { api.pollAccessToken("d") } returnsMany listOf(
            AuthTokenDTO(error = "authorization_pending"), AuthTokenDTO("a", "r", 1, 2)
        )
        val result = repository.awaitAccessToken("d", System.currentTimeMillis() + 10_000, 0)
        assertEquals("a", result.accessToken)
        coVerify(exactly = 2) { api.pollAccessToken("d") }
    }

    @Test fun `poll denied expired and generic errors`() = runTest {
        coEvery { api.pollAccessToken(any()) } returns AuthTokenDTO(error = "access_denied")
        assertEquals(AuthError.DENIED.message, runCatching {
            repository.awaitAccessToken("d", System.currentTimeMillis() + 1000, 0)
        }.exceptionOrNull()?.message?.toIntOrNull())
        coEvery { api.pollAccessToken(any()) } returns AuthTokenDTO(error = "expired_token")
        assertEquals(true, runCatching {
            repository.awaitAccessToken("d", System.currentTimeMillis() + 1000, 0)
        }.exceptionOrNull() is IllegalStateException)
        coEvery { api.pollAccessToken(any()) } returns AuthTokenDTO(error = "other", errorDescription = "bad")
        assertEquals("bad", runCatching {
            repository.awaitAccessToken("d", System.currentTimeMillis() + 1000, 0)
        }.exceptionOrNull()?.message)
    }

    @Test fun `poll expiry without api call throws`() = runTest {
        assertThrows(IllegalStateException::class.java) {
            runTest { repository.awaitAccessToken("d", System.currentTimeMillis() - 1, 1) }
        }
        coVerify(exactly = 0) { api.pollAccessToken(any()) }
    }
}
