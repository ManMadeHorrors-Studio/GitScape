package com.manmadehorrorsstudio.gitscape.data.ai

import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.squareup.moshi.Moshi


class AgentTools(private val repositories: List<GitHubRepo>) : ToolSet {
    private val repoAdapter =
        Moshi.Builder()
            .build()
            .adapter(GitHubRepo::class.java)

    @Tool
    fun list_repos(language: String? = null, max_results: Int = 10): String {
        val limit = max_results.coerceIn(1, 50)
        val result = repositories.asSequence()
            .take(limit)
            .map(::repoJson)
            .toList()
        return "{\"repositories\":[${result.joinToString(",")}]}"
    }

    @Tool
    fun get_repo_summary(repo_name: String): String {
        val repo = repositories.firstOrNull { it.name.equals(repo_name.trim(), ignoreCase = true) }
            ?: return "{\"error\":\"Repository not found in the loaded list\"}"
        return "{\"repository\":${repoJson(repo)}}"
    }

    private fun repoJson(repo: GitHubRepo): String = repoAdapter.toJson(repo)
}