package com.manmadehorrorsstudio.gitscape.domain.usecases

import com.manmadehorrorsstudio.gitscape.domain.models.GitHubReadMe
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.repositories.GitHubRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetReadMeUseCaseTest {
    private val repository = mockk<GitHubRepository>()
    private val useCase = GetGitHubReadMeUseCase(repository)
    private val repo = GitHubRepo("1", "Repo", null, 0, null, null, null)

    @Test
    fun `delegates repository and returns readme`() = runTest {
        val expected = GitHubReadMe("1", "README")
        coEvery { repository.getGitHubReadMe(repo) } returns expected

        assertEquals(expected, useCase(repo))
        coVerify(exactly = 1) { repository.getGitHubReadMe(repo) }
    }

    @Test
    fun `propagates repository failure`() = runTest {
        val failure = IllegalStateException("offline")
        coEvery { repository.getGitHubReadMe(repo) } throws failure

        val actual = runCatching { useCase(repo) }.exceptionOrNull()
        assertEquals(failure, actual)
    }
}
