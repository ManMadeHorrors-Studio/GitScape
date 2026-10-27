package com.manmadehorrorsstudio.gitscape.domain.usecases

import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens
import com.manmadehorrorsstudio.gitscape.domain.repositories.AuthRepository
import javax.inject.Inject

class RefreshAuthTokensUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        refreshToken: String
    ): AuthTokens = authRepository.refreshAccessToken(
        refreshToken = refreshToken
    )
}
