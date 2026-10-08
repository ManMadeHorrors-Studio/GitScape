package com.manmadehorrorsstudio.gitscape.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitHubReadMeDTO(
    val type: String?,
    val encoding: String?,
    val size: Int?,
    val name: String?,
    val path: String?,
    val content: String?,
    val sha: String?,
    @Json(name = "html_url")
    val htmlUrl: String?,
    @Json(name = "download_url")
    val downloadUrl: String?
)

fun GitHubReadMeDTO.isValid(): Boolean {
    return encoding.equals("base64", true) && size != null && size in 1..1_048_576 && !content.isNullOrBlank()
}