package com.manmadehorrorsstudio.gitscape.domain.usecases

import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.repositories.GitHubRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetGitHubRepoListUseCaseTest {
    private lateinit var repository: GitHubRepository
    @BeforeEach fun setUp() { repository = mockk() }
    @Test fun `invoke returns successful repository response`() = runTest {
        val repositories = listOf(
            GitHubRepo(
                "1",
                "GitScape",
                "Kotlin",
                10,
                "https://github.com/example/gitscape",
                null,
                null
            ),
            GitHubRepo(
                "2",
                "TestRepo",
                "Java",
                5,
                "https://github.com/example/testrepo",
                null,
                null
            )
        )
        coEvery {
            repository.getGitHubRepolist()
        } returns repositories
        assertEquals(repositories, GetGitHubRepoListUseCase(repository)())
        coVerify(exactly = 1) {
            repository.getGitHubRepolist()
        }
    }
    @Test fun `invoke propagates repository exception`() = runTest {
        val exception = Exception("Errore di rete")
        coEvery {
            repository.getGitHubRepolist()
        } throws exception
        try {
            GetGitHubRepoListUseCase(repository)()
            error("Expected exception")
        } catch (actual: Exception) {
            assertEquals(exception, actual)
        }
    }
}
