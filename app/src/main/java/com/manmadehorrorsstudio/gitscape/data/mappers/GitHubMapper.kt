package com.manmadehorrorsstudio.gitscape.data.mappers

import com.manmadehorrorsstudio.gitscape.data.local.GitHubRepoEntity
import com.manmadehorrorsstudio.gitscape.data.models.GitHubReadMeDTO
import com.manmadehorrorsstudio.gitscape.data.models.GitHubRepoDTO
import com.manmadehorrorsstudio.gitscape.data.models.GitHubUserDTO
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubReadMe
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser
import kotlin.text.orEmpty

fun GitHubRepoDTO.mapToDomain(): GitHubRepo {
    return GitHubRepo(
        id = id.orEmpty(), //id can't be null because GitHubRepoDTO with null id are filtered out in getGitHubRepolist
        name = name.orEmpty(),
        primaryLanguage = language,
        stars = stargazersCount ?: 0,
        htmlUrl = htmlUrl,
        description = description,
        ownerLogin = owner?.login
    )
}

fun GitHubUserDTO.mapToDomain(): GitHubUser {
    return GitHubUser(
        id = id.orEmpty(), //id can't be null because GitHubUser with null id are filtered out in getGitHubUserDTO.isValid
        userName = name ?: login.orEmpty(), //login cannot be null because GitHubUser with null id or login are filtered out in getGitHubUserDTO.isValid
        avatarUrl = avatarUrl,
        homePageUrl = htmlUrl
    )
}

fun GitHubReadMeDTO.mapToDomain(repoId: String): GitHubReadMe {
    return GitHubReadMe(
        repoId = repoId,
        content = content.orEmpty()
    )
}

fun GitHubRepoEntity.mapToDomain(): GitHubRepo =
    GitHubRepo(
        id = id,
        name = name,
        description = description,
        primaryLanguage = primaryLanguage,
        stars = stars,
        htmlUrl = htmlUrl,
        ownerLogin = ownerLogin
    )

fun GitHubRepo.mapToEntity(): GitHubRepoEntity =
    GitHubRepoEntity(
        id = id,
        name = name,
        description = description,
        primaryLanguage = primaryLanguage,
        stars = stars,
        htmlUrl = htmlUrl,
        ownerLogin = ownerLogin
    )

