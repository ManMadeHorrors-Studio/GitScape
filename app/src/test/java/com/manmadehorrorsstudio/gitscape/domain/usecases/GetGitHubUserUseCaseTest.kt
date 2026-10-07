package com.manmadehorrorsstudio.gitscape.domain.usecases

import com.manmadehorrorsstudio.gitscape.TestUtils.mockedException
import com.manmadehorrorsstudio.gitscape.TestUtils.mockedUser
import com.manmadehorrorsstudio.gitscape.domain.repositories.GitHubRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetGitHubUserUseCaseTest {
    private lateinit var repository: GitHubRepository
    @BeforeEach fun setUp() { repository = mockk() }
    @Test fun `invoke returns successful user`() = runTest {
        coEvery { repository.getGitHubUser() } returns mockedUser
        assertEquals(mockedUser, GetGitHubUserUseCase(repository)())
        coVerify(exactly = 1) { repository.getGitHubUser() }
    }
    @Test fun `invoke propagates error`() = runTest {
        coEvery { repository.getGitHubUser() } throws mockedException
        try { GetGitHubUserUseCase(repository)(); error("Expected exception") } catch (actual: Exception) { assertEquals(mockedException, actual) }
    }
}
