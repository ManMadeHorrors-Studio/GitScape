package com.manmadehorrorsstudio.gitscape.domain.usecases

import com.manmadehorrorsstudio.gitscape.data.models.ApiResponse
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.repositories.GitHubRepository

class GetGitHubRepoListUseCase(
    private val repository: GitHubRepository
) {

    suspend operator fun invoke(): ApiResponse<List<GitHubRepo>> {
        return repository.getGitHubRepolist()
    }
}