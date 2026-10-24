package com.manmadehorrorsstudio.gitscape.domain.models

data class AuthTokens(
    val githubUserId: String,
    val accessToken: String,
    val refreshToken: String?,
    val accessTokenExpiresAt: Long?,
    val refreshTokenExpiresAt: Long?
)