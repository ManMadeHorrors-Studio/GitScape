package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.data.mappers.mapToDomain
import com.manmadehorrorsstudio.gitscape.data.models.ApiResponse
import com.manmadehorrorsstudio.gitscape.data.models.isValid
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser
import com.manmadehorrorsstudio.gitscape.domain.repositories.GitHubRepository

class GitHubRepositoryImpl(
    private val api: GitHubApis
) : GitHubRepository {

    override suspend fun getGitHubRepolist(): ApiResponse<List<GitHubRepo>> {
        return try {
            val result = api.getRepositories()
            val resultMapped = result.filter { it.isValid() }.map { it.mapToDomain() }
            ApiResponse.Success(resultMapped)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun getGitHubUser(): ApiResponse<GitHubUser> {
        return try {
            val result = api.getUser()
            val resultMapped = result.mapToDomain()
            ApiResponse.Success(resultMapped)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }
}