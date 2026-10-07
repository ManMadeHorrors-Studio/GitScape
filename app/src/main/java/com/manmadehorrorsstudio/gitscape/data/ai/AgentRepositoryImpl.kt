package com.manmadehorrorsstudio.gitscape.data.ai

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.GraphAIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.prompt.executor.llms.MultiLLMPromptExecutor
import ai.koog.agents.features.eventHandler.feature.handleEvents
import com.manmadehorrorsstudio.gitscape.BuildConfig
import com.manmadehorrorsstudio.gitscape.LLM_MODEL
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.repositories.AgentRepository
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import javax.inject.Inject

class AgentRepositoryImpl @Inject constructor(
    private val promptExecutor: MultiLLMPromptExecutor
) : AgentRepository {
    override suspend fun ask(
        question: String,
        messagesCount: Int,
        repositories: List<GitHubRepo>): String {
        val key = BuildConfig.AI_KEY.trim()
        check(key.isNotEmpty() && key != "null") {
            "AI API key is not configured."
        }

        val tools = AgentTools(repositories)
        val agent = AIAgent(
            promptExecutor = promptExecutor,
            llmModel = LLM_MODEL,
            toolRegistry = ToolRegistry {
                tools(tools)
            },
            systemPrompt = getBasePrompt(messagesCount),
            maxIterations = 20
        ) {
            logAgentEvents()
        }

        return agent.run(
            "\nQuestion: $question?"
        )
    }

    private fun getBasePrompt(messagesCount: Int): String? {
        return if(messagesCount < 1)
            listOf(
                "You are GitScape's repository assistant. Answer only from tool results and never invent repository facts",
                "If the loaded repositories do not contain the answer, say so",
                "You have access to the loaded repositories and to the conversation history",
                "The user may refer to a specific repository without explicitly naming it",
                "If a question concerns a repository that is not explicitly named, infer the intended repository from the repository or repositories identified in the previous question(s), using the conversation context",
                "Only use this inference when the context clearly indicates which repository or repositories the user is referring to",
                "If the intended repository cannot be determined unambiguously, ask the user to clarify",
                "Always reply in the same language the user used to give you the last question"
            ).joinToString(".\n")
        else
            null
    }

    private fun GraphAIAgent.FeatureContext.logAgentEvents() {
        handleEvents {

            // ─────────────────────────────────────────────
            // AGENT
            // ─────────────────────────────────────────────

            onAgentStarting { event ->
                gitScapeLog(
                    "AI AGENT STARTED: " +
                            "agentId=${event.agent.id}"
                )
            }

            onAgentCompleted { event ->
                gitScapeLog(
                    "AI AGENT COMPLETED: " +
                            "result=${event.result}"
                )
            }

            onAgentExecutionFailed { event ->
                gitScapeLog(
                    "AI AGENT FAILED: " +
                            "error=${event.error.message}"
                )
            }

            onAgentClosing {
                gitScapeLog("AI AGENT CLOSING")
            }


            // ─────────────────────────────────────────────
            // STRATEGY
            // ─────────────────────────────────────────────

            onStrategyStarting { event ->
                gitScapeLog(
                    "AI STRATEGY STARTING: " +
                            event.strategy.name
                )
            }

            onStrategyCompleted { event ->
                gitScapeLog(
                    "AI STRATEGY COMPLETED: " +
                            "result=${event.result}"
                )
            }


            // ─────────────────────────────────────────────
            // NODES
            // ─────────────────────────────────────────────

            onNodeExecutionStarting { event ->
                gitScapeLog(
                    "AI NODE STARTING: " +
                            event.node.name
                )
            }

            onNodeExecutionCompleted { event ->
                gitScapeLog(
                    "AI NODE COMPLETED: " +
                            "${event.node.name} " +
                            "output=${event.output}"
                )
            }

            onNodeExecutionFailed { event ->
                gitScapeLog(
                    "AI NODE FAILED: " +
                            "${event.node.name} " +
                            "error=${event.error.message}"
                )
            }


            // ─────────────────────────────────────────────
            // SUBGRAPHS
            // ─────────────────────────────────────────────

            onSubgraphExecutionStarting { event ->
                gitScapeLog(
                    "AI SUBGRAPH STARTING: " +
                            event.subgraph.name
                )
            }

            onSubgraphExecutionCompleted { event ->
                gitScapeLog(
                    "AI SUBGRAPH COMPLETED: " +
                            "${event.subgraph.name} " +
                            "output=${event.output}"
                )
            }

            onSubgraphExecutionFailed { event ->
                gitScapeLog(
                    "AI SUBGRAPH FAILED: " +
                            "${event.subgraph.name} " +
                            "error=${event.error.message}"
                )
            }


            // ─────────────────────────────────────────────
            // LLM
            // ─────────────────────────────────────────────

            onLLMCallStarting { event ->
                gitScapeLog(
                    "AI LLM CALL STARTING\n" +
                            "model=${event.model}\n" +
                            "prompt=${event.prompt}\n" +
                            "tools=${event.tools}"
                )
            }

            onLLMCallCompleted { event ->
                gitScapeLog(
                    "AI LLM CALL COMPLETED\n" +
                            "model=${event.model}\n" +
                            "responses=${event.response}"
                )
            }


            // ─────────────────────────────────────────────
            // LLM STREAMING
            // ─────────────────────────────────────────────

            onLLMStreamingStarting { event ->
                gitScapeLog(
                    "AI LLM STREAMING STARTING: " +
                            "model=${event.model}"
                )
            }

            onLLMStreamingFrameReceived { event ->
                gitScapeLog(
                    "AI LLM STREAM FRAME: " +
                            "${event.streamFrame}"
                )
            }

            onLLMStreamingCompleted { event ->
                gitScapeLog(
                    "AI LLM STREAMING COMPLETED: " +
                            "model=${event.model}"
                )
            }

            onLLMStreamingFailed { event ->
                gitScapeLog(
                    "AI LLM STREAMING FAILED: " +
                            "${event.error.message}"
                )
            }


            // ─────────────────────────────────────────────
            // TOOLS
            // ─────────────────────────────────────────────

            onToolCallStarting { event ->
                gitScapeLog(
                    "AI TOOL STARTING\n" +
                            "tool=${event.toolName}\n" +
                            "args=${event.toolArgs}"
                )
            }

            onToolCallCompleted { event ->
                gitScapeLog(
                    "AI TOOL COMPLETED\n" +
                            "tool=${event.toolName}\n" +
                            "args=${event.toolArgs}\n" +
                            "result=${event.toolResult}"
                )
            }

            onToolCallFailed { event ->
                gitScapeLog(
                    "AI TOOL FAILED\n" +
                            "tool=${event.toolName}\n" +
                            "args=${event.toolArgs}\n" +
                            "error=${event.error?.message}"
                )
            }

            onToolValidationFailed { event ->
                gitScapeLog(
                    "AI TOOL VALIDATION FAILED\n" +
                            "tool=${event.toolName}\n" +
                            "args=${event.toolArgs}\n" +
                            "error=${event.error.message}"
                )
            }
        }
    }
}
