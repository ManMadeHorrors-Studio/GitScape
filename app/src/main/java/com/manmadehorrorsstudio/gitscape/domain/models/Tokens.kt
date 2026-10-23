package com.manmadehorrorsstudio.gitscape.domain.models

data class Tokens(
    val githubUserId: String,
    val accessToken: String,
    val refreshToken: String?,
    val accessTokenExpiresAt: Long?,
    val refreshTokenExpiresAt: Long?
)