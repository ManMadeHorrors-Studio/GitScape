package com.manmadehorrorsstudio.gitscape.domain.ai

import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo

interface AgentRepository {
    suspend fun ask(question: String, repositories: List<GitHubRepo>): String
}
