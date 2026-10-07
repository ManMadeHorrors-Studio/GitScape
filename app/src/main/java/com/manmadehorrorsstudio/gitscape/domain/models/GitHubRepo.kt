package com.manmadehorrorsstudio.gitscape.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitHubRepo(
    val id: String,
    val name: String,
    val primaryLanguage: String?,
    val stars: Int,
    val htmlUrl: String?
)