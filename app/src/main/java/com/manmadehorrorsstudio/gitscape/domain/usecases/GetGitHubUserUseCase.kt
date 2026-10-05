package com.manmadehorrorsstudio.gitscape.domain.usecases

import com.manmadehorrorsstudio.gitscape.data.models.ApiResponse
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser
import com.manmadehorrorsstudio.gitscape.domain.repositories.GitHubRepository

class GetGitHubUserUseCase (
    private val repository: GitHubRepository
) {

    suspend operator fun invoke(): ApiResponse<GitHubUser> {
        return repository.getGitHubUser()
    }
}