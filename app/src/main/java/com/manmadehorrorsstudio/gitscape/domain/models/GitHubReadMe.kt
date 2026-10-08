package com.manmadehorrorsstudio.gitscape.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitHubReadMe(
    val repoId: String,
    val content: String
)