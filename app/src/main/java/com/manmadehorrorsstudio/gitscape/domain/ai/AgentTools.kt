package com.manmadehorrorsstudio.gitscape.domain.ai

import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo


class AgentTools(private val repositories: List<GitHubRepo>) : ToolSet {
    @Tool
    fun list_repos(language: String? = null, max_results: Int = 10): String {
        val limit = max_results.coerceIn(1, 50)
        val result = repositories.asSequence()
            .filter { language.isNullOrBlank() || it.primaryLanguage.equals(language.trim(), ignoreCase = true) }
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

    private fun repoJson(repo: GitHubRepo): String =
        "{\"name\":${json(repo.name)},\"stars\":${repo.stars}," +
                "\"primary_language\":${json(repo.primaryLanguage ?: "unknown")}," +
                "\"url\":${json(repo.htmlUrl ?: "")}}"

    private fun json(value: String): String =
        "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
}