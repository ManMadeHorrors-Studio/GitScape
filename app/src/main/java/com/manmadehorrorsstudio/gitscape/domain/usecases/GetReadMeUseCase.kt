package com.manmadehorrorsstudio.gitscape.domain.usecases

import com.manmadehorrorsstudio.gitscape.domain.models.GitHubReadMe
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.repositories.GitHubRepository

class GetGitHubReadMeUseCase (
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(gitHubRepo: GitHubRepo): GitHubReadMe = repository.getGitHubReadMe(gitHubRepo)
}
