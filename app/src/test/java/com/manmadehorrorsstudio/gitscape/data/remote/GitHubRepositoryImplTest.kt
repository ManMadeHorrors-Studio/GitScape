package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.TestUtils.mockedException
import com.manmadehorrorsstudio.gitscape.data.models.GitHubRepoDTO
import com.manmadehorrorsstudio.gitscape.data.models.GitHubUserDTO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GitHubRepositoryImplTest {
    private lateinit var api: GitHubApis
    private lateinit var repository: GitHubRepositoryImpl

    @BeforeEach fun setUp() {
        api = mockk()
        repository = GitHubRepositoryImpl(api)
    }

    @Test fun `getGitHubRepoList returns valid repositories mapped and sorted`() = runTest {
        val repoShort = GitHubRepoDTO(
            "1",
            "App",
            "Kotlin",
            10,
            "https://github.com/test/app"
        )
        val repoLong = GitHubRepoDTO(
            "2",
            "VeryLongRepository",
            "Java",
            20,
            "https://github.com/test/verylongrepository"
        )
        coEvery { api.getRepositories() } returns listOf(repoShort, repoLong)
        val repositories = repository.getGitHubRepolist()
        assertEquals(
            2,
            repositories.size
        )
        assertEquals(
            repoLong.name,
            repositories[0].name
        )
        assertEquals(
            repoShort.name,
            repositories[1].name)
        assertEquals(
            repoLong.stargazersCount,
            repositories[0].stars
        )
        coVerify(exactly = 1) {
            api.getRepositories()
        }
    }

    @Test fun `getGitHubRepoList ignores invalid repositories`() = runTest {
        val validRepo = GitHubRepoDTO(
            "1",
            "ValidRepo",
            "Kotlin",
            10,
            "https://github.com/test/valid"
        )
        val invalidRepo = GitHubRepoDTO(
            null,
            null,
            null,
            null,
            null
        )
        coEvery { api.getRepositories() } returns listOf(validRepo, invalidRepo)
        val repositories = repository.getGitHubRepolist()
        assertEquals(
            1,
            repositories.size
        )
        assertEquals(
            validRepo.name,
            repositories[0].name
        )
    }

    @Test fun `getGitHubRepoList throws when api throws exception`() = runTest {
        coEvery { api.getRepositories() } throws mockedException
        try {
            repository.getGitHubRepolist()
            error("Expected exception")
        } catch (actual: Exception) {
            assertEquals(mockedException, actual)
        }
        coVerify(exactly = 1) {
            api.getRepositories()
        }
    }

    @Test fun `getGitHubUser returns mapped user`() = runTest {
        val dto = GitHubUserDTO(
            id = "123",
            login = "man-made-horrors-studio",
            name = "Man Made Horrors Studio",
            avatarUrl = "https://github.com/avatar.jpg"
        )
        coEvery { api.getUser() } returns dto
        val user = repository.getGitHubUser()
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

    @Test fun `getGitHubUser throws when user is invalid`() = runTest {
        coEvery { api.getUser() } returns GitHubUserDTO(
            id = null,
            name = null,
            avatarUrl = null
        )
        try {
            repository.getGitHubUser(); error("Expected exception")
        } catch (actual: Exception) {
            assertEquals("Invalid user", actual.message)
        }
    }

    @Test fun `getGitHubUser throws when api throws exception`() = runTest {
        coEvery { api.getUser() } throws mockedException
        try {
            repository.getGitHubUser(); error("Expected exception")
        } catch (actual: Exception) {
            assertEquals(mockedException, actual)
        }
    }
}
