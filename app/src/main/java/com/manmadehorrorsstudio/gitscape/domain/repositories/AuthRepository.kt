package com.manmadehorrorsstudio.gitscape.domain.repositories

import com.manmadehorrorsstudio.gitscape.domain.models.AuthDeviceCode
import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens

interface AuthRepository {
    suspend fun requestAuthDeviceCode(): AuthDeviceCode
    suspend fun awaitAccessToken(
        deviceCode: String,
        deviceCodeExpiresAt: Long,
        initialInterval: Int
    ): AuthTokens
    suspend fun refreshAccessToken(refreshToken: String): AuthTokens
}