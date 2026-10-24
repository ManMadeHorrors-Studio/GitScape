package com.manmadehorrorsstudio.gitscape.domain.repositories

import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens

interface TokensRepository {
    suspend fun getTokens(): AuthTokens?
    suspend fun saveTokens(authTokens: AuthTokens)
    suspend fun replaceTokens(authTokens: AuthTokens)
    suspend fun deleteTokens()
}