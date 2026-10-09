package com.manmadehorrorsstudio.gitscape.domain.models

import com.squareup.moshi.JsonClass
import java.security.MessageDigest
import kotlin.reflect.full.memberProperties

@JsonClass(generateAdapter = true)
data class GitHubRepo(
    val id: String,
    val name: String,
    val primaryLanguage: String?,
    val stars: Int,
    val htmlUrl: String?,
    val description: String?,
    val ownerLogin: String?
)

fun List<GitHubRepo>.calculateHash(): String {
    val memberProperties = GitHubRepo::class.memberProperties
    val canonicalData = buildString {
        this@calculateHash.forEach { repo ->
            for (proprieta in memberProperties) {
                append("${proprieta.name}-${proprieta.get(repo) ?: "null"}")
            }

        }
    }
    val bytes = MessageDigest.getInstance("SHA-256").digest(canonicalData.toByteArray(Charsets.UTF_8))
    return bytes.joinToString("") { "%02x".format(it) }
}