package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.data.local.AuthCache
import com.manmadehorrorsstudio.gitscape.GITHUB_API_BASE_PATH
import com.manmadehorrorsstudio.gitscape.GITHUB_GET_USER_PATH
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GitHubAuthInterceptorTest {

    private lateinit var interceptor: GitHubAuthInterceptor
    private lateinit var chain: Interceptor.Chain
    private lateinit var response: Response
    private val tockenMocked = "tocken_mocked"

    @BeforeEach
    fun setUp() {
        val authCache = AuthCache().apply { updateAccessToken(token = tockenMocked) }
        interceptor = GitHubAuthInterceptor(authCache)
        chain = mockk()
        response = mockk()
    }

    @Test
    fun `GitHub request adds authentication headers`() {

        val request = Request.Builder()
            .url("https://$GITHUB_API_BASE_PATH/$GITHUB_GET_USER_PATH")
            .build()

        every {
            chain.request()
        } returns request

        every {
            chain.proceed(any())
        } returns response

        val capturedRequest = slot<Request>()

        every {
            chain.proceed(capture(capturedRequest))
        } returns response

        interceptor.intercept(chain)

        val authenticatedRequest = capturedRequest.captured

        assertEquals(
            "Bearer $tockenMocked",
            authenticatedRequest.header("Authorization")
        )

        assertEquals(
            "application/vnd.github+json",
            authenticatedRequest.header("Accept")
        )

        assertEquals(
            "2026-03-10",
            authenticatedRequest.header("X-GitHub-Api-Version")
        )
    }

    @Test
    fun `non GitHub request is not authenticated`() {

        val request = Request.Builder()
            .url("https://example.com/test")
            .build()

        every {
            chain.request()
        } returns request

        every {
            chain.proceed(request)
        } returns response

        interceptor.intercept(chain)

        verify(exactly = 1) {
            chain.proceed(request)
        }

        assertNull(
            request.header("Authorization")
        )

        assertNull(
            request.header("Accept")
        )

        assertNull(
            request.header("X-GitHub-Api-Version")
        )
    }
}