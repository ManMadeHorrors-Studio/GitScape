package com.manmadehorrorsstudio.gitscape.domain.usecases

import com.manmadehorrorsstudio.gitscape.data.models.ApiResponse
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

    @BeforeEach
    fun setUp() {
        repository = mockk()
    }

    @Test
    fun `invoke returns successful repository response`() = runTest {

        val repositories = listOf(
            GitHubRepo(
                id = "1",
                name = "GitScape",
                primaryLanguage = "Kotlin",
                stars = 10,
                htmlUrl = "https://github.com/example/gitscape"
            ),
            GitHubRepo(
                id = "2",
                name = "TestRepo",
                primaryLanguage = "Java",
                stars = 5,
                htmlUrl = "https://github.com/example/testrepo"
            )
        )

        val expectedResponse = ApiResponse.Success(repositories)

        coEvery {
            repository.getGitHubRepolist()
        } returns expectedResponse

        val useCase = GetGitHubRepoListUseCase(repository)

        val result = useCase()

        assertEquals(
            expectedResponse,
            result
        )

        coVerify(exactly = 1) {
            repository.getGitHubRepolist()
        }
    }

    @Test
    fun `invoke returns error repository response`() = runTest {

        val exception = Exception("Errore di rete")

        val expectedResponse = ApiResponse.Error(
            exception
        )

        coEvery {
            repository.getGitHubRepolist()
        } returns expectedResponse

        val useCase = GetGitHubRepoListUseCase(repository)

        val result = useCase()

        assertEquals(
            expectedResponse,
            result
        )

        coVerify(exactly = 1) {
            repository.getGitHubRepolist()
        }
    }
}