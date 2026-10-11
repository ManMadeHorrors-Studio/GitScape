package com.manmadehorrorsstudio.gitscape.data.ai

import com.manmadehorrorsstudio.gitscape.TestUtils.mockGitScapeLog
import com.manmadehorrorsstudio.gitscape.TestUtils.unmockGitScapeLog
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubReadMe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GitHubReadMeCacheTest {
    @BeforeEach fun setUp() = mockGitScapeLog()
    @AfterEach fun tearDown() = unmockGitScapeLog()

    @Test
    fun `loads each repository once and returns cached value`() = runTest {
        val cache = GitHubReadMeCache()
        val expected = GitHubReadMe("repo-1", "content")
        var loads = 0

        val first = cache.getOrLoad("repo-1") { loads++; expected }
        val second = cache.getOrLoad("repo-1") { loads++; GitHubReadMe("repo-1", "other") }

        assertEquals(expected, first)
        assertEquals(expected, second)
        assertEquals(1, loads)
    }

    @Test
    fun `different repository ids have independent entries`() = runTest {
        val cache = GitHubReadMeCache()
        val first = cache.getOrLoad("one") { GitHubReadMe("one", "1") }
        val second = cache.getOrLoad("two") { GitHubReadMe("two", "2") }

        assertEquals(GitHubReadMe("one", "1"), first)
        assertEquals(GitHubReadMe("two", "2"), second)
    }

    @Test
    fun `loader failure is not cached`() = runTest {
        val cache = GitHubReadMeCache()
        var loads = 0
        runCatching { cache.getOrLoad("repo") { loads++; error("offline") } }
        val value = cache.getOrLoad("repo") { loads++; GitHubReadMe("repo", "ok") }

        assertEquals(GitHubReadMe("repo", "ok"), value)
        assertEquals(2, loads)
    }
}
