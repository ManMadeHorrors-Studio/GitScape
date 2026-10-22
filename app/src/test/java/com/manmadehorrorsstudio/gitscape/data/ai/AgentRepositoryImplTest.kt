package com.manmadehorrorsstudio.gitscape.data.ai
import ai.koog.agents.core.tools.reflect.ToolFromCallable
import ai.koog.agents.core.tools.reflect.asTool
import ai.koog.agents.testing.tools.getMockExecutor
import com.manmadehorrorsstudio.gitscape.BuildConfig
import com.manmadehorrorsstudio.gitscape.LIST_REPOS_NAME
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubReadMeUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse

class AgentRepositoryImplTest {

    // API KEY

    @Test
    fun `valid API key is accepted`() {
        assertDoesNotThrow {
            validateAiKey("test-api-key")
        }
    }

    @Test
    fun `API key with surrounding whitespace is accepted`() {
        assertDoesNotThrow {
            validateAiKey("  test-api-key  ")
        }
    }

    @Test
    fun `empty API key throws exception`() {
        val exception = assertThrows(
            IllegalStateException::class.java
        ) {
            validateAiKey("")
        }

        assertEquals(
            "AI API key is not configured.",
            exception.message
        )
    }

    @Test
    fun `whitespace-only API key throws exception`() {
        assertThrows(IllegalStateException::class.java) {
            validateAiKey("   ")
        }
    }

    @Test
    fun `API key containing null throws exception`() {
        assertThrows(IllegalStateException::class.java) {
            validateAiKey(" null ")
        }
    }

    // SYSTEM PROMPT

    @Test
    fun `base prompt is created for a new conversation`() {
        val prompt = getBasePrompt(0)

        assertNotNull(prompt)
        assertTrue(
            prompt!!.contains("never invent repository facts")
        )
        assertTrue(
            prompt.contains("Always reply in the same language")
        )
    }

    @Test
    fun `base prompt is omitted when conversation has history`() {
        val prompt = getBasePrompt(2)

        assertNull(prompt)
    }

    //ASK

    @Test
    fun `ask loads repositories and returns the final answer`() = runTest {
        // La validazione della chiave viene già testata separatamente.
        assertFalse(
            BuildConfig.AI_KEY.isBlank() ||
                    BuildConfig.AI_KEY.trim() == "null"
        )

        val marker = "TEST_REPO_MARKER"
        val expectedAnswer = "Risposta finale del test"

        val repo = mockk<GitHubRepo>()

        every { repo.id } returns "123"
        every { repo.name } returns marker
        every { repo.primaryLanguage } returns "Kotlin"
        every { repo.stars } returns 10
        every { repo.htmlUrl } returns "https://github.com/test/repository"
        every { repo.description } returns "Test repository"
        every { repo.ownerLogin } returns "test-owner"

        val repositories = listOf(repo)
        val readMeUseCase = mockk<GetGitHubReadMeUseCase>(relaxed = true)

        // Serve a costruire il tool usato per configurare la risposta simulata.
        val toolsForMock = GitScapeAgentTools(
            getGitHubReadMeUseCase = readMeUseCase,
            repositories = repositories,
            onUiCommand = {}
        )

        val listReposTool = GitScapeAgentTools::list_repos.asTool(
            thisRef = toolsForMock,
            name = LIST_REPOS_NAME
        )

        val mockExecutor = getMockExecutor {
            // Il risultato del tool deve essere presente prima
            // che il modello restituisca la risposta finale.
            mockLLMAnswer(expectedAnswer) onRequestContains marker

            mockLLMToolCall(
                listReposTool,
                ToolFromCallable.Args(emptyMap())
            ) onRequestContains
                    "Load the current repository list using list_repos before answering the user."

            mockLLMAnswer("Risposta di fallback").asDefaultResponse
        }

        val repository = AgentRepositoryImpl(
            promptExecutor = mockExecutor
        )

        val result = repository.ask(
            messages = emptyList(),
            getGitHubReadMeUseCase = readMeUseCase,
            repositories = repositories,
            onUiCommand = {}
        )

        assertEquals(expectedAnswer, result)
    }
}