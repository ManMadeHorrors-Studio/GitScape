package com.manmadehorrorsstudio.gitscape.domain.repositories

import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import kotlinx.coroutines.flow.Flow

interface LocalGitHubRepository {
    suspend fun getGitHubRepositories(): List<GitHubRepo>
    suspend fun saveGitHubRepositories(repoList: List<GitHubRepo>)
    suspend fun clearGitHubRepositories()
}