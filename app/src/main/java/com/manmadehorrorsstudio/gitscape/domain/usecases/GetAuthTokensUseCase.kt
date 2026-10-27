package com.manmadehorrorsstudio.gitscape.domain.usecases

import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens
import com.manmadehorrorsstudio.gitscape.domain.repositories.AuthRepository
import javax.inject.Inject

class GetAuthTokensUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        deviceCode: String,
        deviceCodeExpiresAt: Long,
        initialInterval: Int
    ): AuthTokens = authRepository.awaitAccessToken(
        deviceCode = deviceCode,
        deviceCodeExpiresAt = deviceCodeExpiresAt,
        initialInterval = initialInterval
    )
}