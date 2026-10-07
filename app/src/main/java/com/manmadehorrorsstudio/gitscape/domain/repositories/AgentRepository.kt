package com.manmadehorrorsstudio.gitscape.domain.repositories

import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo

interface AgentRepository {
    suspend fun ask(
        question: String,
        messagesCount: Int,
        repositories: List<GitHubRepo>): String
}
