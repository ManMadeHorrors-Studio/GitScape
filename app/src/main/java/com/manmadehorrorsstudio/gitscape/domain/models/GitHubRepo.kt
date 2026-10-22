package com.manmadehorrorsstudio.gitscape.domain.models

import com.squareup.moshi.JsonClass
import java.security.MessageDigest

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
    val canonicalData = buildString {
        fun appendField(field: String) {
            append("${field.length}:$field-")
        }

        this@calculateHash.forEach { repo ->
            appendField(repo.id)
            appendField(repo.name)
            appendField(repo.primaryLanguage ?: "null")
            appendField(repo.stars.toString())
            appendField(repo.htmlUrl ?: "null")
            appendField(repo.description ?: "null")
            appendField(repo.ownerLogin ?: "null")
        }
    }

    val bytes = MessageDigest.getInstance("SHA-256").digest(canonicalData.toByteArray(Charsets.UTF_8))
    return bytes.joinToString("") { "%02x".format(it) }
}
