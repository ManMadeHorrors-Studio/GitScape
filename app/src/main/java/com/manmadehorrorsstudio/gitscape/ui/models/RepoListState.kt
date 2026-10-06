package com.manmadehorrorsstudio.gitscape.ui.models

import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo

data class RepoListState(
    val repositories: List<GitHubRepo> = emptyList(),
    val isLoading: Boolean = false
)