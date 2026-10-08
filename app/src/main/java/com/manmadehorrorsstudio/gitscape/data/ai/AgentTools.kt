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
        return repositories.firstOrNull { it.name.equals(repo_name.trim(), ignoreCase = true) }?.let { repo ->
            "{\"repository\":${repoJson(repo)}}"
        } ?: "{\"error\":\"Repository with name $repo_name not found in the loaded list\"}"
    }

    @Tool
    @LLMDescription("Provides a specific repository by its id")
    fun get_repo_by_id(
        @LLMDescription("The id of the repository you are looking for")
        repo_id: String
    ): String {
        return getRepoById(repo_id)?.let { repo ->
            "{\"repository\":${repoJson(repo)}}"
        } ?: noRepoFound(repo_id)
    }

    @Tool
    @LLMDescription("When the user asks you to point, to select, or to show a specific repository in the app, you can use this tool")
    fun point_to_repo_by_id(
        @LLMDescription("The id of the repository you have to point, to select, or to show")
        repo_id: String
    ): String {
        return getRepoById(repo_id)?.let { repo ->
            onUiCommand(AgentUiCommand.PointToRepo(repo.id))
            "Repository with id:$repo_id pointed with success"
        } ?: noRepoFound(repo_id)
    }

    private fun getRepoById(repoId: String): GitHubRepo? =
        repositories.firstOrNull { repo ->
            repo.id.equals(repoId.trim(), ignoreCase = true)
        }

    private fun noRepoFound(repo_id: String) = "{\"error\":\"Repository with id:$repo_id not found in the loaded list\"}"

    private val repoAdapter by lazy {
        Moshi.Builder()
            .build()
            .adapter(GitHubRepo::class.java)
    }
    private fun repoJson(repo: GitHubRepo): String = repoAdapter.toJson(repo)
}