package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.manmadehorrorsstudio.gitscape.GITHUB_REPOSITORIES_TABLE_NAME


@Entity(tableName = GITHUB_REPOSITORIES_TABLE_NAME)
data class GitHubRepoEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val primaryLanguage: String?,
    val stars: Int,
    val htmlUrl: String?,
    val description: String?,
    val ownerLogin: String?
)