package com.manmadehorrorsstudio.gitscape.data.ai

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import com.manmadehorrorsstudio.gitscape.LIST_REPOS_NAME
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubReadMeUseCase
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.ai.AgentUiCommand
import com.squareup.moshi.Moshi


class GitScapeAgentTools(
    private val getGitHubReadMeUseCase: GetGitHubReadMeUseCase,
    private val repositories: List<GitHubRepo>,
    private val onUiCommand: (AgentUiCommand) -> Unit
) : ToolSet {

    @Tool(customName = LIST_REPOS_NAME)
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

    @Tool
    @LLMDescription(
        "Retrieves the README content of a specific list of repositories. " +
            "Use this tool only when you need more specific information about one or more " +
            "repositories than the information already provided by list_repos. " +
            "For example, use it when you need detailed repository information from the " +
            "README content to answer the user's question. " +
            "Do not use this tool when the available repository metadata is sufficient. " +
            "You can request information for up to 10 repositories at a time."
    )
    suspend fun get_repos_readme(
        @LLMDescription(
            "List of repository IDs to retrieve more specific information for. " +
                "Provide at most 10 repository IDs."
        )
        repo_ids: List<String>
    ): String {

        val ids = repo_ids
            .asSequence()
            .map(String::trim)
            .filter(String::isNotBlank)
            .distinct()
            .take(10)
            .toList()

        if (ids.isEmpty()) {
            return """{"error":"No valid repository IDs provided"}"""
        }

        val result = ids.map { repoId ->

            getRepoById(repoId)?.let { repo ->
                try {
                    val readMe = getGitHubReadMeUseCase(repo)
                    """{
                        "repo_id":${repo.id.toJson()},
                        "name":${repo.name.toJson()},
                        "description":${repo.description?.toJson() ?: "unknown"},
                        "readme":${readMe.content.toJson()}
                    }""".trimIndent()

                    } catch (e: Exception) {
                        gitScapeLog(
                            "get_repos_readme error for ${repo.id}",
                            e
                        )

                        """{
                            "repo_id":${repo.id.toJson()},
                            "name":${repo.name.toJson()},
                            "error":"Unable to retrieve repository information"
                        }""".trimIndent()
                    }
                } ?: noRepoFound(repoId)
            }
        return """{"repositories":[${result.joinToString(",")}]}"""
    }

    private fun getRepoById(repoId: String): GitHubRepo? =
        repositories.firstOrNull { repo ->
            repo.id.equals(repoId.trim(), ignoreCase = true)
        }

    private fun noRepoFound(repo_id: String) = "{\"error\":\"Repository with id:$repo_id not found in the loaded list\"}"

    private fun String?.toJson(): String? =
        this?.let { questo ->
            "\"" +
            questo
                .replace("\\", "\\\\")
                .replace("\"", "\\\"") +
            "\""
        }

    private val repoAdapter by lazy {
        Moshi.Builder()
            .build()
            .adapter(GitHubRepo::class.java)
    }
    private fun repoJson(repo: GitHubRepo): String = repoAdapter.toJson(repo)
}