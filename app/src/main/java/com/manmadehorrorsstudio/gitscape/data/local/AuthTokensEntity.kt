package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.manmadehorrorsstudio.gitscape.TOKENS_TABLE_NAME

@Entity(tableName = TOKENS_TABLE_NAME)
data class AuthTokensEntity(
    @PrimaryKey
    val githubUserId: String,

    val accessToken: String,

    val refreshToken: String,

    val accessTokenExpiresAt: Long?,

    val refreshTokenExpiresAt: Long?
)