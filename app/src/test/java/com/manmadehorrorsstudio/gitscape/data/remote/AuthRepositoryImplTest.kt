package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.AUTHDEVICECODE_SAFETY_TIME
import com.manmadehorrorsstudio.gitscape.TestUtils.mockGitScapeLog
import com.manmadehorrorsstudio.gitscape.TestUtils.unmockGitScapeLog
import com.manmadehorrorsstudio.gitscape.addCurrentTime
import com.manmadehorrorsstudio.gitscape.isNotExpired
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

    @BeforeEach
    fun setUp() {
        mockGitScapeLog()
        api = mockk()
        repository = AuthRepositoryImpl(api)
    }

    @AfterEach
    fun tearDown() = unmockGitScapeLog()

    @Test
    fun `request device maps valid response with epoch expiry`() = runTest {
        coEvery { api.requestDeviceCode() } returns AuthDeviceCodeDTO("d", "u", "url", 60L, 1)

        val result = repository.requestAuthDeviceCode()

        assertEquals("d", result.deviceCode)
        assertEquals(1, result.interval)
        assert(result.expiresAt.isNotExpired())
    }

    @Test
    fun `request device rejects invalid response`() = runTest {
        coEvery { api.requestDeviceCode() } returns AuthDeviceCodeDTO()

        assertThrows(Exception::class.java) {
            runTest { repository.requestAuthDeviceCode() }
        }
    }

    @Test
    fun `refresh maps valid response using epoch expiration`() = runTest {
        coEvery { api.refreshAccessToken("r") } returns AuthTokenDTO("a", "rt", 10L, 20L)

        val result = repository.refreshAccessToken("r")

        assertEquals("a", result.accessToken)
        assertEquals("rt", result.refreshToken)
        assert(result.accessTokenExpiresAt.isNotExpired())
    }

    @Test
    fun `refresh rejects invalid response`() = runTest {
        coEvery { api.refreshAccessToken("bad") } returns AuthTokenDTO()

        assertThrows(Exception::class.java) {
            runTest { repository.refreshAccessToken("bad") }
        }
    }

    @Test
    fun `poll pending then succeeds`() = runTest {
        coEvery { api.pollAccessToken("d") } returnsMany listOf(
            AuthTokenDTO(error = "authorization_pending"),
            AuthTokenDTO("a", "r", 10L, 20L)
        )

        val result = repository.awaitAccessToken(
            "d",
            10L.addCurrentTime(AUTHDEVICECODE_SAFETY_TIME)!!,
            0
        )

        assertEquals("a", result.accessToken)
        coVerify(exactly = 2) { api.pollAccessToken("d") }
    }

    @Test
    fun `poll denied expired and generic errors`() = runTest {
        coEvery { api.pollAccessToken(any()) } returns AuthTokenDTO(error = "access_denied")
        assertEquals(
            AuthError.DENIED.message,
            runCatching {
                repository.awaitAccessToken("d", 1L.addCurrentTime(AUTHDEVICECODE_SAFETY_TIME)!!, 0)
            }.exceptionOrNull()?.message?.toIntOrNull()
        )

        coEvery { api.pollAccessToken(any()) } returns AuthTokenDTO(error = "expired_token")
        assert(runCatching {
            repository.awaitAccessToken("d", 1L.addCurrentTime(AUTHDEVICECODE_SAFETY_TIME)!!, 0)
        }.exceptionOrNull() is IllegalStateException)

        coEvery { api.pollAccessToken(any()) } returns AuthTokenDTO(error = "other", errorDescription = "bad")
        assertEquals("bad", runCatching {
            repository.awaitAccessToken("d", 1L.addCurrentTime(AUTHDEVICECODE_SAFETY_TIME)!!, 0)
        }.exceptionOrNull()?.message)
    }

    @Test
    fun `poll expiry without api call throws`() = runTest {
        assertThrows(IllegalStateException::class.java) {
            runTest {
                repository.awaitAccessToken("d", (-1L).addCurrentTime(AUTHDEVICECODE_SAFETY_TIME)!!, 1)
            }
        }
        coVerify(exactly = 0) { api.pollAccessToken(any()) }
    }

}
