package com.manmadehorrorsstudio.gitscape.domain.usecases

import com.manmadehorrorsstudio.gitscape.TestUtils.mockedException
import com.manmadehorrorsstudio.gitscape.TestUtils.mockedUser
import com.manmadehorrorsstudio.gitscape.data.models.ApiResponse
import com.manmadehorrorsstudio.gitscape.domain.repositories.GitHubRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetGitHubUserUseCaseTest {

    private lateinit var repository: GitHubRepository

    @BeforeEach
    fun setUp() {
        repository = mockk()
    }

    @Test
    fun `invoke returns successful user response`() = runTest {

        val expectedResponse = ApiResponse.Success(mockedUser)

        coEvery {
            repository.getGitHubUser()
        } returns expectedResponse

        val useCase = GetGitHubUserUseCase(repository)

        val result = useCase()

        assertEquals(
            expectedResponse,
            result
        )

        coVerify(exactly = 1) {
            repository.getGitHubUser()
        }
    }

    @Test
    fun `invoke returns error response`() = runTest {

        val expectedResponse = ApiResponse.Error((mockedException))

        coEvery {
            repository.getGitHubUser()
        } returns expectedResponse

        val useCase = GetGitHubUserUseCase(repository)

        val result = useCase()

        assertEquals(
            expectedResponse,
            result
        )

        coVerify(exactly = 1) {
            repository.getGitHubUser()
        }
    }
}