package com.manmadehorrorsstudio.gitscape.domain.repositories

import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo


interface GitHubRepository {

    suspend fun getGitHubRepolist(): List<GitHubRepo>
}