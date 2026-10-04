package com.manmadehorrorsstudio.gitscape.data.mappers

import com.manmadehorrorsstudio.gitscape.data.models.GitHubRepoDTO
import com.manmadehorrorsstudio.gitscape.data.models.GitHubUserDTO
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser
import kotlin.text.orEmpty

fun GitHubRepoDTO.mapToDomain(): GitHubRepo {
    return GitHubRepo(
        id = id.orEmpty(), //id can't be null because GitHubRepoDTO with null id are filtered out in getGitHubRepolist
        name = name.orEmpty(),
        primaryLanguage = language,
        stars = stargazersCount ?: 0,
        htmlUrl = htmlUrl
    )
}

fun GitHubUserDTO.mapToDomain(): GitHubUser {
    return GitHubUser(
        id = id ?: "Unknown",
        userName = name,
        avatarUrl = avatarUrl
    )
}