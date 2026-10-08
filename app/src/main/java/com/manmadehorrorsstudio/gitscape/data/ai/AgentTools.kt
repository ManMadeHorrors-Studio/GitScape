package com.manmadehorrorsstudio.gitscape.data.ai

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.ui.ai.AgentUiCommand
import com.squareup.moshi.Moshi


class AgentTools(
    private val repositories: List<GitHubRepo>,
    private val onUiCommand: (AgentUiCommand) -> Unit
) : ToolSet {

    @Tool
    @LLMDescription("Provides the repositories required for your tasks")
    fun list_repos(): String {
        val result = repositories.asSequence()
            .map(::repoJson)
            .toList()
        return "{\"repositories\":[${result.joinToString(",")}]}"
    }

    @Tool
    @LLMDescription("Provides a specific repository by its name")
    fun get_repo_by_name(
        @LLMDescription("The name of the repository you are looking for")
        repo_name: String
    ): String {
        val repo = repositories.firstOrNull { it.name.equals(repo_name.trim(), ignoreCase = true) }
            ?: return "{\"error\":\"Repository not found in the loaded list\"}"
        return "{\"repository\":${repoJson(repo)}}"
    }

    @Tool
    @LLMDescription("Provides a specific repository by its id")
    fun get_repo_by_id(
        @LLMDescription("The id of the repository you are looking for")
        repo_id: String
    ): String {
        val repo = repositories.firstOrNull { it.id.equals(repo_id.trim(), ignoreCase = true) }
            ?: return "{\"error\":\"Repository not found in the loaded list\"}"
        return "{\"repository\":${repoJson(repo)}}"
    }

    @Tool
    @LLMDescription("When the user asks you to point, to select, or to show a specific repository in the app, you can use this tool")
    fun point_to_repo_by_id(
        @LLMDescription("The id of the repository you have to point, to select, or to show")
        repo_id: String
    ): String {
        return repositories.firstOrNull { it.id.equals(repo_id.trim(), ignoreCase = true) }?.let { repo ->
            onUiCommand(AgentUiCommand.PointToRepo(repo.id))
            "Repository with id:$repo_id pointed with success"
        } ?: "{\"error\":\"Repository with id:$repo_id not found\"}"
    }

    private val repoAdapter by lazy {
        Moshi.Builder()
            .build()
            .adapter(GitHubRepo::class.java)
    }
    private fun repoJson(repo: GitHubRepo): String = repoAdapter.toJson(repo)
}