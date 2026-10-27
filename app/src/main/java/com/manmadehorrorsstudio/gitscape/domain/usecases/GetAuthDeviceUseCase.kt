package com.manmadehorrorsstudio.gitscape.domain.usecases


import com.manmadehorrorsstudio.gitscape.domain.models.AuthDeviceCode
import com.manmadehorrorsstudio.gitscape.domain.repositories.AuthRepository
import javax.inject.Inject

class GetAuthDeviceUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): AuthDeviceCode {
        return authRepository.requestAuthDeviceCode()
    }
}