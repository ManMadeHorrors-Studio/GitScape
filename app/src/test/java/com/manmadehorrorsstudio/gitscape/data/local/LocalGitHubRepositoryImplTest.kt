package com.manmadehorrorsstudio.gitscape.data.local

import com.manmadehorrorsstudio.gitscape.data.mappers.mapToEntity
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LocalGitHubRepositoryImplTest {
    private val dao = mockk<GitHubDao>()
    private val repository = LocalGitHubRepositoryImpl(dao)
    private val repo = GitHubRepo("1", "GitScape", "Kotlin", 4, "url", "description", "owner")

    @Test
    fun `get repositories maps database entities to domain models`() = runTest {
        coEvery { dao.getRepositories() } returns listOf(
            GitHubRepoEntity("1", "GitScape", "Kotlin", 4, "url", "description", "owner")
        )

        assertEquals(listOf(repo), repository.getGitHubRepositories())
    }

    @Test
    fun `save clears existing data and persists mapped entities`() = runTest {
        coEvery { dao.clearRepositories() } returns Unit
        coEvery { dao.saveRepositories(any()) } returns Unit

        repository.saveGitHubRepositories(listOf(repo))

        coVerify(exactly = 1) { dao.clearRepositories() }
        coVerify(exactly = 1) { dao.saveRepositories(listOf(repo.mapToEntity())) }
    }

    @Test
    fun `saving empty list clears without inserting`() = runTest {
        coEvery { dao.clearRepositories() } returns Unit

        repository.saveGitHubRepositories(emptyList())

        coVerify(exactly = 1) { dao.clearRepositories() }
        coVerify(exactly = 0) { dao.saveRepositories(any()) }
    }

    @Test
    fun `clear delegates to dao`() = runTest {
        coEvery { dao.clearRepositories() } returns Unit

        repository.clearGitHubRepositories()

        coVerify(exactly = 1) { dao.clearRepositories() }
    }
}
