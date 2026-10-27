package com.manmadehorrorsstudio.gitscape.ui.viewmodels

import com.manmadehorrorsstudio.gitscape.TestUtils.mockGitScapeLog
import com.manmadehorrorsstudio.gitscape.TestUtils.mockedException
import com.manmadehorrorsstudio.gitscape.TestUtils.unmockGitScapeLog
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalGitHubRepository
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubRepoListUseCase
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepoListViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repos: GetGitHubRepoListUseCase
    private lateinit var local: LocalGitHubRepository
    private lateinit var settings: SettingsRepository

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repos = mockk()
        local = mockk(relaxed = true)
        settings = mockk()
        coEvery { settings.getRepoListHash() } returns null
        every { settings.getRepoListEnabled() } returns MutableStateFlow(false)
        mockGitScapeLog()
    }

    @AfterEach
    fun tearDown() {
        unmockGitScapeLog()
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization does not load repositories until refresh`() = runTest {
        val vm = RepoListViewModel(repos, local, settings)
        advanceUntilIdle()

        coVerify(exactly = 0) { repos() }
        assertTrue(vm.repoListstate.value.repositories.isEmpty())
        assertFalse(vm.repoListstate.value.isLoading)
    }

    @Test
    fun `refresh loads repositories successfully`() = runTest {
        val repositories = listOf(
            GitHubRepo("1", "GitScape", "Kotlin", 10,
                "https://github.com/example/gitscape", null, null)
        )
        coEvery { repos() } returns repositories

        val vm = RepoListViewModel(repos, local, settings)
        vm.refreshRepoListScreen()
        advanceUntilIdle()

        assertEquals(repositories, vm.repoListstate.value.repositories)
        assertFalse(vm.repoListstate.value.isLoading)
        coVerify(exactly = 1) { repos() }
    }

    @Test
    fun `refresh error clears loading and emits error event`() = runTest {
        coEvery { repos() } throws mockedException

        val vm = RepoListViewModel(repos, local, settings)
        vm.refreshRepoListScreen()
        advanceUntilIdle()

        assertFalse(vm.repoListstate.value.isLoading)
        assertTrue(vm.repoListEvents.first() is com.manmadehorrorsstudio.gitscape.ui.models.UIEvents.ShowError)
    }

    @Test
    fun `refresh saves repositories when local repository cache is enabled`() = runTest {
        val repositories = listOf(
            GitHubRepo("1", "GitScape", "Kotlin", 10,
                "https://github.com/example/gitscape", null, null)
        )
        coEvery { repos() } returns repositories
        every { settings.getRepoListEnabled() } returns MutableStateFlow(true)
        coEvery { settings.saveRepoListHash(any()) } returns Unit

        val vm = RepoListViewModel(repos, local, settings)
        vm.refreshRepoListScreen()
        advanceUntilIdle()

        coVerify(exactly = 1) { local.saveGitHubRepositories(repositories) }
        coVerify(exactly = 1) { settings.saveRepoListHash(any()) }
    }
}
