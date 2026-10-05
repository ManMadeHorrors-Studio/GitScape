package com.manmadehorrorsstudio.gitscape.domain.repositories

import com.manmadehorrorsstudio.gitscape.data.models.ApiResponse
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser


interface GitHubRepository {

    suspend fun getGitHubRepolist(): ApiResponse<List<GitHubRepo>>
    suspend fun getGitHubUser(): ApiResponse<GitHubUser>
}