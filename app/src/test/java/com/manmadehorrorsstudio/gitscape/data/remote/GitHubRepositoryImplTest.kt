package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.TestUtils.mockedException
import com.manmadehorrorsstudio.gitscape.data.ai.GitHubReadMeCache
import com.manmadehorrorsstudio.gitscape.data.models.GitHubReadMeDTO
import com.manmadehorrorsstudio.gitscape.data.models.GitHubRepoDTO
import com.manmadehorrorsstudio.gitscape.TestUtils.mockGitScapeLog
import com.manmadehorrorsstudio.gitscape.TestUtils.unmockGitScapeLog
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
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
    private lateinit var readMeCache: GitHubReadMeCache
    private lateinit var repository: GitHubRepositoryImpl

    @BeforeEach fun setUp() {
        mockGitScapeLog()
        api = mockk()
        readMeCache = GitHubReadMeCache()
        repository = GitHubRepositoryImpl(api, readMeCache)
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        unmockGitScapeLog()
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

    @Test fun `getGitHubReadMe loads and maps README`() = runTest {
        val repo = GitHubRepo(
            id = "repo-1",
            name = "GitScape",
            description = null,
            primaryLanguage = "Kotlin",
            stars = 3,
            htmlUrl = "https://github.com/user/gitscape",
            ownerLogin = "user"
        )
        coEvery { api.getGitHubReadMe("user", "GitScape") } returns GitHubReadMeDTO(
            type = "file", encoding = "base64", size = 4, name = "README.md",
            path = "README.md", content = "cmVhZA==", sha = "sha", htmlUrl = null, downloadUrl = null
        )

        val readMe = repository.getGitHubReadMe(repo)

        assertEquals("repo-1", readMe.repoId)
        assertEquals("cmVhZA==", readMe.content)
        coVerify(exactly = 1) { api.getGitHubReadMe("user", "GitScape") }
    }

    @Test fun `getGitHubReadMe uses cache and does not call API twice`() = runTest {
        val repo = GitHubRepo(
            id = "repo-1", name = "GitScape", description = null,
            primaryLanguage = "Kotlin", stars = 3,
            htmlUrl = "https://github.com/user/gitscape", ownerLogin = "user"
        )
        coEvery { api.getGitHubReadMe(any(), any()) } returns GitHubReadMeDTO(
            type = "file", encoding = "base64", size = 4, name = "README.md",
            path = "README.md", content = "cmVhZA==", sha = "sha", htmlUrl = null, downloadUrl = null
        )

        val first = repository.getGitHubReadMe(repo)
        val second = repository.getGitHubReadMe(repo)

        assertEquals(first, second)
        coVerify(exactly = 1) { api.getGitHubReadMe("user", "GitScape") }
    }

    @Test fun `getGitHubReadMe throws when owner login is missing`() = runTest {
        val repo = GitHubRepo(
            id = "repo-1", name = "GitScape", description = null,
            primaryLanguage = "Kotlin", stars = 3,
            htmlUrl = null, ownerLogin = null
        )

        try {
            repository.getGitHubReadMe(repo)
            error("Expected exception")
        } catch (actual: Exception) {
            assertEquals("Missing owner login", actual.message)
        }
        coVerify(exactly = 0) { api.getGitHubReadMe(any(), any()) }
    }
}
