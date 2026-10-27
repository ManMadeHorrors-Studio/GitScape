package com.manmadehorrorsstudio.gitscape.data.local

import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens
import com.manmadehorrorsstudio.gitscape.data.mappers.toEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LocalTokensRepositoryImplTest {
    private val dao = mockk<AuthTokensDao>()
    private val repository = LocalTokensRepositoryImpl(dao)
    private val tokens = AuthTokens("id", "access", "refresh", 1, 2)

    @Test fun `get maps entity and returns null`() = runTest {
        coEvery { dao.getTokens() } returns tokens.toEntity()
        assertEquals(tokens, repository.getTokens())
        coEvery { dao.getTokens() } returns null
        assertEquals(null, repository.getTokens())
    }

    @Test fun `save replace and delete delegate`() = runTest {
        coEvery { dao.saveTokens(any()) } returns Unit
        coEvery { dao.replaceTokens(any()) } returns Unit
        coEvery { dao.deleteAllTokens() } returns Unit
        repository.saveTokens(tokens)
        repository.replaceTokens(tokens)
        repository.deleteTokens()
        coVerify { dao.saveTokens(tokens.toEntity()) }
        coVerify { dao.replaceTokens(tokens.toEntity()) }
        coVerify { dao.deleteAllTokens() }
    }
}
