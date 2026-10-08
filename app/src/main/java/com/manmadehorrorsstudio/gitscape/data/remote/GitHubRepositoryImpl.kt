package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.data.mappers.mapToDomain
import com.manmadehorrorsstudio.gitscape.data.models.isValid
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser
import com.manmadehorrorsstudio.gitscape.domain.repositories.GitHubRepository

class GitHubRepositoryImpl(
    private val api: GitHubApis
) : GitHubRepository {

    override suspend fun getGitHubRepolist(): List<GitHubRepo> = run {
        api.getRepositories()
            .filter { it.isValid() }
            .map { it.mapToDomain() }
    }

    override suspend fun getGitHubUser(): GitHubUser = run {
        val result = api.getUser()
        if (!result.isValid())
            throw Exception("Invalid user")
        result.mapToDomain()
    }
}
