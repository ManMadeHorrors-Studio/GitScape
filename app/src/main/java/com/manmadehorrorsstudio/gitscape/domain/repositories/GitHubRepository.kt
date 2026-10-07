package com.manmadehorrorsstudio.gitscape.domain.repositories

import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser

interface GitHubRepository {

    suspend fun getGitHubRepolist(): List<GitHubRepo>
    suspend fun getGitHubUser(): GitHubUser
}
