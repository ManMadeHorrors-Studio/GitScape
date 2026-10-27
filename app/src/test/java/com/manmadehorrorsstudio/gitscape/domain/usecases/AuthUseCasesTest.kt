package com.manmadehorrorsstudio.gitscape.domain.usecases

import com.manmadehorrorsstudio.gitscape.domain.models.AuthDeviceCode
import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens
import com.manmadehorrorsstudio.gitscape.domain.repositories.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AuthUseCasesTest {
    private val repository = mockk<AuthRepository>()
    private val device = AuthDeviceCode("d", "u", "url", 100, 1)
    private val tokens = AuthTokens("id", "a", "r", 1, 2)

    @Test fun `get device delegates`() = runTest {
        coEvery { repository.requestAuthDeviceCode() } returns device
        assertEquals(device, GetAuthDeviceUseCase(repository)())
        coVerify { repository.requestAuthDeviceCode() }
    }

    @Test fun `get tokens delegates all arguments`() = runTest {
        coEvery { repository.awaitAccessToken("d", 100, 3) } returns tokens
        assertEquals(tokens, GetAuthTokensUseCase(repository)("d", 100, 3))
        coVerify { repository.awaitAccessToken("d", 100, 3) }
    }

    @Test fun `refresh tokens delegates`() = runTest {
        coEvery { repository.refreshAccessToken("r") } returns tokens
        assertEquals(tokens, RefreshAuthTokensUseCase(repository)("r"))
        coVerify { repository.refreshAccessToken("r") }
    }
}
