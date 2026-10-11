package com.manmadehorrorsstudio.gitscape.ui.viewmodels

import com.manmadehorrorsstudio.gitscape.TestUtils.mockGitScapeLog
import com.manmadehorrorsstudio.gitscape.TestUtils.mockedException
import com.manmadehorrorsstudio.gitscape.TestUtils.mockedUser
import com.manmadehorrorsstudio.gitscape.TestUtils.unmockGitScapeLog
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubRepoListUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubUserUseCase
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalGitHubRepository
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import com.manmadehorrorsstudio.gitscape.ui.models.UIEvents
import io.mockk.coEvery
import io.mockk.every
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepoListViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repos: GetGitHubRepoListUseCase
    private lateinit var user: GetGitHubUserUseCase
    private lateinit var local: LocalGitHubRepository
    private lateinit var settings: SettingsRepository

    @BeforeEach fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repos = mockk(); user = mockk(); local = mockk(relaxed = true); settings = mockk()
        coEvery { settings.getRepoListHash() } returns null
        every { settings.getRepoListEnabled() } returns MutableStateFlow(false)
        mockGitScapeLog()
    }
    @AfterEach fun tearDown() { unmockGitScapeLog(); Dispatchers.resetMain() }

    @Test fun `initialization loads repositories and user`() = runTest {
        coEvery { repos() } returns emptyList()
        coEvery { user() } returns mockedUser
        RepoListViewModel(
            repos,
            user,
            local,
            settings
        )
        advanceUntilIdle()
        coVerify(exactly = 1) { repos() }
        coVerify(exactly = 1) { user() }
    }
    @Test fun `repository list is loaded successfully`() = runTest {
        val repositories = listOf(
            GitHubRepo(
                "1",
                "GitScape",
                "Kotlin",
                10,
                "https://github.com/example/gitscape",
                null,
                null
            )
        )
        coEvery { repos() } returns repositories
        coEvery { user() } returns mockedUser
        val vm = RepoListViewModel(repos, user, local, settings)
        advanceUntilIdle()
        assertEquals(repositories, vm.repoListstate.value.repositories)
        assertFalse(vm.repoListstate.value.isLoading)
    }
    @Test fun `repository error clears loading and emits error event`() = runTest {
        coEvery { repos() } throws mockedException
        coEvery { user() } returns mockedUser
        val vm = RepoListViewModel(
            repos,
            user,
            local,
            settings
        )
        advanceUntilIdle()
        assertFalse(vm.repoListstate.value.isLoading)
        val event = vm.repoListEvents.first()
        assertTrue(event is UIEvents.ShowError)
        assertTrue(event is UIEvents.ShowError)
    }
    @Test fun `user is loaded successfully`() = runTest {
        coEvery { repos() } returns emptyList()
        coEvery { user() } returns mockedUser
        val vm = RepoListViewModel(
            repos,
            user,
            local,
            settings)
        advanceUntilIdle()
        assertEquals(
            mockedUser,
            vm.userState.value
        )
    }
    @Test fun `refresh reloads repositories but not user`() = runTest {
        coEvery { repos() } returns emptyList()
        coEvery { user() } returns mockedUser
        val vm = RepoListViewModel(
            repos, user, local, settings
        )
        advanceUntilIdle()
        vm.refreshRepoListScreen()
        advanceUntilIdle()
        coVerify(exactly = 2) { repos() }
        coVerify(exactly = 1) { user() }
    }
}
