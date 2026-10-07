package com.manmadehorrorsstudio.gitscape.domain.repositories

import ai.koog.prompt.message.Message
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo

interface AgentRepository {
    suspend fun ask(
        messages: List<Message>,
        repositories: List<GitHubRepo>
    ) : String
}
