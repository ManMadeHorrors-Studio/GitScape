package com.manmadehorrorsstudio.gitscape.domain.repositories

import com.manmadehorrorsstudio.gitscape.domain.models.GitHubReadMe
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser

interface GitHubRepository {

    suspend fun getGitHubRepolist(): List<GitHubRepo>
    suspend fun getGitHubUser(): GitHubUser
    suspend fun getGitHubReadMe(gitHubRepo: GitHubRepo): GitHubReadMe
}
