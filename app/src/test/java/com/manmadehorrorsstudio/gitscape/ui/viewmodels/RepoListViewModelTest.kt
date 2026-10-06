package com.manmadehorrorsstudio.gitscape.ui.viewmodels


import com.manmadehorrorsstudio.gitscape.TestUtils.mockGitScapeLog
import com.manmadehorrorsstudio.gitscape.TestUtils.mockedException
import com.manmadehorrorsstudio.gitscape.TestUtils.mockedUser
import com.manmadehorrorsstudio.gitscape.TestUtils.unmockGitScapeLog
import com.manmadehorrorsstudio.gitscape.data.models.ApiResponse
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubRepoListUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubUserUseCase
import com.manmadehorrorsstudio.gitscape.ui.models.UIEvents
import io.mockk.coEvery
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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class RepoListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getGitHubRepoListUseCase: GetGitHubRepoListUseCase
    private lateinit var getGitHubUserUseCase: GetGitHubUserUseCase

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getGitHubRepoListUseCase = mockk()
        getGitHubUserUseCase = mockk()

        mockGitScapeLog()
    }

    @AfterEach
    fun tearDown() {
        unmockGitScapeLog()
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization loads repositories and user`() = runTest {

        coEvery {
            getGitHubRepoListUseCase()
        } returns ApiResponse.Success(emptyList())

        coEvery {
            getGitHubUserUseCase()
        } returns ApiResponse.Success(
            mockedUser
        )

        val viewModel = RepoListViewModel(
            getGitHubRepoListUseCase,
            getGitHubUserUseCase
        )

        advanceUntilIdle()

        coVerify(exactly = 1) {
            getGitHubRepoListUseCase()
        }

        coVerify(exactly = 1) {
            getGitHubUserUseCase()
        }
    }

    @Test
    fun `repository list is loaded successfully`() = runTest {

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

        coEvery {
            getGitHubRepoListUseCase()
        } returns ApiResponse.Success(repositories)

        coEvery {
            getGitHubUserUseCase()
        } returns ApiResponse.Success(
            mockedUser
        )

        val viewModel = RepoListViewModel(
            getGitHubRepoListUseCase,
            getGitHubUserUseCase
        )

        advanceUntilIdle()

        assertEquals(
            repositories,
            viewModel.repoListstate.value.repositories
        )

        assertFalse(
            viewModel.repoListstate.value.isLoading
        )
    }

    @Test
    fun `repository error clears loading and emits error event`() = runTest {

        coEvery {
            getGitHubRepoListUseCase()
        } returns ApiResponse.Error(mockedException)

        coEvery {
            getGitHubUserUseCase()
        } returns ApiResponse.Success(
            mockedUser
        )

        val viewModel = RepoListViewModel(
            getGitHubRepoListUseCase,
            getGitHubUserUseCase
        )

        advanceUntilIdle()

        assertFalse(
            viewModel.repoListstate.value.isLoading
        )

        val event = viewModel.repoListEvents.first()

        assertTrue(
            event is UIEvents.ShowError
        )

        assertEquals(
            mockedException.message,
            (event as UIEvents.ShowError).message
        )
    }

    @Test
    fun `user is loaded successfully`() = runTest {

        coEvery {
            getGitHubRepoListUseCase()
        } returns ApiResponse.Success(emptyList())

        coEvery {
            getGitHubUserUseCase()
        } returns ApiResponse.Success(mockedUser)

        val viewModel = RepoListViewModel(
            getGitHubRepoListUseCase,
            getGitHubUserUseCase
        )

        advanceUntilIdle()

        assertEquals(
            mockedUser,
            viewModel.userState.value
        )
    }

    @Test
    fun `refresh reloads repositories but not user`() = runTest {

        coEvery {
            getGitHubRepoListUseCase()
        } returns ApiResponse.Success(emptyList())

        coEvery {
            getGitHubUserUseCase()
        } returns ApiResponse.Success(mockedUser)

        val viewModel = RepoListViewModel(
            getGitHubRepoListUseCase,
            getGitHubUserUseCase
        )

        advanceUntilIdle()

        viewModel.refreshRepoListScreen()

        advanceUntilIdle()

        coVerify(exactly = 2) {
            getGitHubRepoListUseCase()
        }

        coVerify(exactly = 1) {
            getGitHubUserUseCase()
        }
    }
}