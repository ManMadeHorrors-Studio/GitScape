package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.TestUtils.mockedException
import com.manmadehorrorsstudio.gitscape.data.models.ApiResponse
import com.manmadehorrorsstudio.gitscape.data.models.GitHubRepoDTO
import com.manmadehorrorsstudio.gitscape.data.models.GitHubUserDTO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GitHubRepositoryImplTest {

    private lateinit var api: GitHubApis
    private lateinit var repository: GitHubRepositoryImpl

    @BeforeEach
    fun setUp() {
        api = mockk()

        repository = GitHubRepositoryImpl(
            api = api
        )
    }

    @Test
    fun `getGitHubRepoList returns valid repositories mapped and sorted`() = runTest {

        val repoShort = GitHubRepoDTO(
            id = "1",
            name = "App",
            language = "Kotlin",
            stargazersCount = 10,
            htmlUrl = "https://github.com/test/app"
        )

        val repoLong = GitHubRepoDTO(
            id = "2",
            name = "VeryLongRepository",
            language = "Java",
            stargazersCount = 20,
            htmlUrl = "https://github.com/test/verylongrepository"
        )

        coEvery {
            api.getRepositories()
        } returns listOf(
            repoShort,
            repoLong
        )

        val result = repository.getGitHubRepolist()

        assertTrue(result is ApiResponse.Success)

        val repositories = (result as ApiResponse.Success).data

        assertEquals(2, repositories.size)

        assertEquals(
            repoLong.name,
            repositories[0].name
        )

        assertEquals(
            repoShort.name,
            repositories[1].name
        )

        assertEquals(
            repoLong.stargazersCount,
            repositories[0].stars
        )

        coVerify(exactly = 1) {
            api.getRepositories()
        }
    }

    @Test
    fun `getGitHubRepoList ignores invalid repositories`() = runTest {

        val validRepo = GitHubRepoDTO(
            id = "1",
            name = "ValidRepo",
            language = "Kotlin",
            stargazersCount = 10,
            htmlUrl = "https://github.com/test/valid"
        )

        val invalidRepo = GitHubRepoDTO(
            id = null,
            name = null,
            language = null,
            stargazersCount = null,
            htmlUrl = null
        )

        coEvery {
            api.getRepositories()
        } returns listOf(
            validRepo,
            invalidRepo
        )

        val result = repository.getGitHubRepolist()

        assertTrue(result is ApiResponse.Success)

        val repositories = (result as ApiResponse.Success).data

        assertEquals(1, repositories.size)

        assertEquals(
            validRepo.name,
            repositories[0].name
        )
    }

    @Test
    fun `getGitHubRepoList returns error when api throws exception`() = runTest {

        coEvery {
            api.getRepositories()
        } throws mockedException

        val result = repository.getGitHubRepolist()

        assertTrue(result is ApiResponse.Error)

        assertEquals(
            mockedException,
            (result as ApiResponse.Error).exception
        )

        coVerify(exactly = 1) {
            api.getRepositories()
        }
    }

    @Test
    fun `getGitHubUser returns mapped user`() = runTest {

        val dto = GitHubUserDTO(
            id = "123",
            name = "Man Made Horrors",
            avatarUrl = "https://github.com/avatar.jpg"
        )

        coEvery {
            api.getUser()
        } returns dto

        val result = repository.getGitHubUser()

        assertTrue(result is ApiResponse.Success)

        val user = (result as ApiResponse.Success).data

        assertEquals(
            dto.id,
            user.id
        )

        assertEquals(
            dto.name,
            user.userName
        )

        assertEquals(
            dto.avatarUrl,
            user.avatarUrl
        )

        coVerify(exactly = 1) {
            api.getUser()
        }
    }

    @Test
    fun `getGitHubUser returns error when user is invalid`() = runTest {

        val invalidUser = GitHubUserDTO(
            id = null,
            name = null,
            avatarUrl = null
        )

        coEvery {
            api.getUser()
        } returns invalidUser

        val result = repository.getGitHubUser()

        assertTrue(result is ApiResponse.Error)

        assertEquals(
            "Invalid user",
            (result as ApiResponse.Error).exception.message
        )
    }

    @Test
    fun `getGitHubUser returns error when api throws exception`() = runTest {

        coEvery {
            api.getUser()
        } throws mockedException

        val result = repository.getGitHubUser()

        assertTrue(result is ApiResponse.Error)

        assertEquals(
            mockedException,
            (result as ApiResponse.Error).exception
        )
    }
}