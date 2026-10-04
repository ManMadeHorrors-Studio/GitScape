package com.manmadehorrorsstudio.gitscape.domain.models

data class GitHubRepo(
    val id: Long,
    val name: String,
    val primaryLanguage: String?,
    val stars: Int,
    val htmlUrl: String?
)