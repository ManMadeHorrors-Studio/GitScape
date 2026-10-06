package com.manmadehorrorsstudio.gitscape.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitHubUserDTO (
    val login: String? = null,
    val id: String? = null,
    @Json(name = "avatar_url")
    val avatarUrl: String? = null,
    @Json(name = "html_url")
    val htmlUrl: String? = null,
    val name: String? = null,
    val company: String? = null,
    val bio: String? = null,
    @Json(name = "public_repos")
    val publicRepos: Int? = null,
    val followers: Int? = null,
    val following: Int? = null
)

fun GitHubUserDTO.isValid(): Boolean {
    return id != null && name != null
}