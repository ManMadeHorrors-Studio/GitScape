package com.manmadehorrorsstudio.gitscape.data.ai

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.reflect.ToolFromCallable
import ai.koog.agents.core.tools.reflect.asTool
import ai.koog.agents.testing.tools.getMockExecutor
import com.manmadehorrorsstudio.gitscape.LIST_REPOS_NAME
import com.manmadehorrorsstudio.gitscape.LLM_MODEL
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GitScapeStrategyTest {

    companion object {
        private const val TEST_REPO_MARKER = "TEST_REPOSITORY_MARKER"
        private const val FINAL_ANSWER = "Risposta finale del test"
        private const val UNEXPECTED_RESPONSE = "Risposta inattesa"
    }

    @Test
    fun `list_repos is executed before final answer`() = runTest {

        // Repository fittizio per verificare il risultato del tool.
        val repo = mockk<GitHubRepo>()

        every { repo.id } returns "123"
        every { repo.name } returns TEST_REPO_MARKER
        every { repo.primaryLanguage } returns "Kotlin"
        every { repo.stars } returns 10
        every { repo.htmlUrl } returns "https://github.com/test/repository"
        every { repo.description } returns "Test repository"
        every { repo.ownerLogin } returns "test-owner"

        val gitScapeAgentTools = GitScapeAgentTools(
            getGitHubReadMeUseCase = mockk(relaxed = true),
            repositories = listOf(repo),
            onUiCommand = {}
        )

        // Registriamo i tool reali dell'applicazione.
        val toolRegistry = ToolRegistry {
            tools(gitScapeAgentTools)
        }

        // Recuperiamo il tool registrato da Koog.
        val listReposTool = GitScapeAgentTools::list_repos.asTool(
            thisRef = gitScapeAgentTools,
            name = LIST_REPOS_NAME
        )

        // Mock del modello: prima chiama list_repos,
        // poi risponde solo se nel contesto compare il repository di test.
        val mockExecutor = getMockExecutor {

            mockLLMAnswer(FINAL_ANSWER) onRequestContains
                    TEST_REPO_MARKER

            mockLLMToolCall(
                listReposTool,
                ToolFromCallable.Args(emptyMap())
            ) onRequestContains
                    "Load the current repository list"

            mockLLMAnswer(UNEXPECTED_RESPONSE).asDefaultResponse
        }

        // La strategia da testare è quella effettiva di GitScape.
        val repository = AgentRepositoryImpl(
            promptExecutor = mockk(relaxed = true)
        )

        val agent = AIAgent(
            promptExecutor = mockExecutor,
            llmModel = LLM_MODEL,
            toolRegistry = toolRegistry,
            systemPrompt = "Test agent",
            maxIterations = 20,
            strategy = repository.gitScapeStrategy(
                listReposToolDescriptor = listReposTool.descriptor
            )
        )

        val result = agent.run(
            agentInput = emptyList()
        )

        assertEquals(FINAL_ANSWER, result)
    }
}