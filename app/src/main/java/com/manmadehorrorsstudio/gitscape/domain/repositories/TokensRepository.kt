package com.manmadehorrorsstudio.gitscape.domain.repositories

import com.manmadehorrorsstudio.gitscape.domain.models.Tokens

interface TokensRepository {
    suspend fun getTokens(): Tokens?
    suspend fun saveTokens(tokens: Tokens)
    suspend fun replaceTokens(tokens: Tokens)
    suspend fun deleteTokens()
}