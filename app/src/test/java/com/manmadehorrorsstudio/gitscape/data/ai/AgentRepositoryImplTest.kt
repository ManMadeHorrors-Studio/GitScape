package com.manmadehorrorsstudio.gitscape.data.ai
import ai.koog.agents.core.tools.reflect.ToolFromCallable
import ai.koog.agents.core.tools.reflect.asTool
import ai.koog.agents.testing.tools.getMockExecutor
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import com.manmadehorrorsstudio.gitscape.domain.models.LlmConfiguration
import com.manmadehorrorsstudio.gitscape.LIST_REPOS_NAME
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubReadMeUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AgentRepositoryImplTest {

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

}