package com.manmadehorrorsstudio.gitscape.data.ai

import com.manmadehorrorsstudio.gitscape.TestUtils.mockGitScapeLog
import com.manmadehorrorsstudio.gitscape.TestUtils.unmockGitScapeLog
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubReadMe
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubReadMeUseCase
import com.manmadehorrorsstudio.gitscape.ui.ai.AgentUiCommand
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GitScapeAgentToolsTest {
    @org.junit.jupiter.api.BeforeEach fun setUp() = mockGitScapeLog()
    @org.junit.jupiter.api.AfterEach fun tearDown() = unmockGitScapeLog()
    private val readMe = mockk<GetGitHubReadMeUseCase>()
    private val repo = GitHubRepo(
        id = "42", name = "GitScape", primaryLanguage = "Kotlin", stars = 7,
        htmlUrl = "https://github.com/example/gitscape", description = "an app", ownerLogin = "owner"
    )

    @Test
    fun `list repos returns empty valid collection`() {
        val tools = GitScapeAgentTools(readMe, emptyList()) { }
        assertEquals("{\"repositories\":[]}", tools.list_repos())
    }

    @Test
    fun `list repos and lookup are case insensitive`() {
        val tools = GitScapeAgentTools(readMe, listOf(repo)) { }
        val listed = tools.list_repos()
        assertEquals(true, listed.contains("GitScape"))
        assertEquals(true, tools.get_repo_by_name(" gItScApE ").contains("\"id\":\"42\""))
        assertEquals(true, tools.get_repo_by_id(" 42 ").contains("GitScape"))
    }

    @Test
    fun `missing repository returns structured error`() {
        val tools = GitScapeAgentTools(readMe, listOf(repo)) { }
        assertEquals(true, tools.get_repo_by_id("missing").contains("not found"))
        assertEquals(true, tools.get_repo_by_name("missing").contains("not found"))
    }

    @Test
    fun `point tool emits command only for existing repository`() {
        var command: AgentUiCommand? = null
        val tools = GitScapeAgentTools(readMe, listOf(repo)) { command = it }
        assertEquals(true, tools.point_to_repo_by_id("42").contains("success"))
        assertEquals(AgentUiCommand.PointToRepo("42"), command)
        assertEquals(true, tools.point_to_repo_by_id("nope").contains("not found"))
    }

    @Test
    fun `readme tool handles empty ids, limits ids and reports failures`() = runTest {
        val second = repo.copy(id = "43", name = "Second")
        val tools = GitScapeAgentTools(readMe, listOf(repo, second)) { }
        assertEquals("{\"error\":\"No valid repository IDs provided\"}", tools.get_repos_readme(listOf(" ")))
        coEvery { readMe(repo) } returns GitHubReadMe(repo.id, "README")
        coEvery { readMe(second) } throws IllegalStateException("offline")
        val result = tools.get_repos_readme(listOf("42", "42", "43", "unknown"))
        assertEquals(true, result.contains("README"))
        assertEquals(true, result.contains("Unable to retrieve repository information"))
        assertEquals(true, result.contains("not found"))
    }
}
