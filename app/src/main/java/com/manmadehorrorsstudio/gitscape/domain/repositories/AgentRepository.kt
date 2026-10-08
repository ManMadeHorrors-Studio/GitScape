package com.manmadehorrorsstudio.gitscape.domain.repositories

import ai.koog.prompt.message.Message
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.ui.ai.AgentUiCommand

interface AgentRepository {
    suspend fun ask(
        messages: List<Message>,
        repositories: List<GitHubRepo>,
        onUiCommand: (AgentUiCommand) -> Unit
    ) : String
}
