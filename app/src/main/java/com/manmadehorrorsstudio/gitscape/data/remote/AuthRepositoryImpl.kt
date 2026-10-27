package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.data.mappers.mapToDomain
import com.manmadehorrorsstudio.gitscape.data.models.AuthTokenDTO
import com.manmadehorrorsstudio.gitscape.data.models.isValid
import com.manmadehorrorsstudio.gitscape.domain.models.AuthDeviceCode
import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens
import com.manmadehorrorsstudio.gitscape.domain.repositories.AuthRepository
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.models.AuthError
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApis
) : AuthRepository {

    override suspend fun requestAuthDeviceCode(): AuthDeviceCode  {
        val result = api.requestDeviceCode()
        if (!result.isValid())
            throw Exception("Invalid auth device code")
        return result.mapToDomain()
    }

    override suspend fun awaitAccessToken(
        deviceCode: String,
        deviceCodeExpiresAt: Long,
        initialInterval: Int
    ): AuthTokens {
        var intervalSeconds = initialInterval

        var pending = 0

        while (System.currentTimeMillis() < deviceCodeExpiresAt) {
            delay((intervalSeconds * 1000).milliseconds)

            val response: AuthTokenDTO = api.pollAccessToken(
                deviceCode = deviceCode
            )

            if (response.isValid()) {
                return response.mapToDomain("")
            }

            when (AuthError.fromType(response.error)) {
                AuthError.PENDING ->
                    gitScapeLog("awaitAccessToken pending for ${++pending} times...")

                AuthError.SLOW_DOWN ->
                    intervalSeconds += 5

                AuthError.DENIED ->
                    throw IllegalStateException(AuthError.DENIED.message.toString())

                AuthError.EXPIRED ->
                    throw IllegalStateException(AuthError.EXPIRED.message.toString())

                AuthError.GENERIC ->
                    throw IllegalStateException(
                        response.errorDescription
                            ?: response.error
                            ?: AuthError.GENERIC.message.toString()
                    )
                else -> {
                    //Do nothing
                }
            }
        }

        throw IllegalStateException(
            AuthError.EXPIRED.message.toString()
        )
    }

    override suspend fun refreshAccessToken(refreshToken: String): AuthTokens {
        val result = api.refreshAccessToken(refreshToken)
        if (!result.isValid())
            throw Exception("Invalid auth device code")
        return result.mapToDomain("")
    }


}