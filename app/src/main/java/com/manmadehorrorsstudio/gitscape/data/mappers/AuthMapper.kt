package com.manmadehorrorsstudio.gitscape.data.mappers

import com.manmadehorrorsstudio.gitscape.data.local.AuthTokensEntity
import com.manmadehorrorsstudio.gitscape.data.models.AuthDeviceCodeDTO
import com.manmadehorrorsstudio.gitscape.data.models.AuthTokenDTO
import com.manmadehorrorsstudio.gitscape.domain.models.AuthDeviceCode
import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens

fun AuthDeviceCodeDTO.mapToDomain(): AuthDeviceCode =
    AuthDeviceCode( //no value can be null because AuthDeviceCodeDTO with null values are filtered out in requestDeviceCode
        deviceCode = deviceCode.orEmpty(),
        userCode = userCode.orEmpty(),
        verificationUri = verificationUri.orEmpty(),
        expiresAt = expiresIn?.let {System.currentTimeMillis() + it * 1_000} ?: -1,
        interval = interval ?: -1
    )

fun AuthTokensEntity.mapToDomain(): AuthTokens =
    AuthTokens(
        githubUserId = githubUserId,
        accessToken = accessToken,
        refreshToken = refreshToken,
        accessTokenExpiresAt = accessTokenExpiresAt,
        refreshTokenExpiresAt = refreshTokenExpiresAt
    )

fun AuthTokenDTO.mapToDomain(githubUserId: String): AuthTokens =
    AuthTokens(
        githubUserId = githubUserId,
        accessToken = accessToken.orEmpty(),    //cannot be null because AuthTokenDTO with null accessToken are filtered out in AuthRepositoryImpl
        refreshToken = refreshToken.orEmpty(),  //cannot be null because AuthTokenDTO with null refreshToken are filtered out in AuthRepositoryImpl
        accessTokenExpiresAt = accessTokenExpiresIn,
        refreshTokenExpiresIn
    )

fun AuthTokens.toEntity(): AuthTokensEntity =
    AuthTokensEntity(
        githubUserId = githubUserId,
        accessToken = accessToken,
        refreshToken = refreshToken,
        accessTokenExpiresAt = accessTokenExpiresAt,
        refreshTokenExpiresAt = refreshTokenExpiresAt
    )

