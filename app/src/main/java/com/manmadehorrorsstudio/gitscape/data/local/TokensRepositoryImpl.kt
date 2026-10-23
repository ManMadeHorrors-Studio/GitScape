package com.manmadehorrorsstudio.gitscape.data.local

import com.manmadehorrorsstudio.gitscape.data.mappers.toDomain
import com.manmadehorrorsstudio.gitscape.data.mappers.toEntity
import com.manmadehorrorsstudio.gitscape.domain.models.Tokens
import com.manmadehorrorsstudio.gitscape.domain.repositories.TokensRepository
import javax.inject.Inject

class TokensRepositoryImpl @Inject constructor(
    private val tokensDao: TokensDao
) : TokensRepository {

    override suspend fun getTokens(): Tokens? {
        return tokensDao.getTokens()?.toDomain()
    }

    override suspend fun saveTokens(tokens: Tokens) {
        tokensDao.saveTokens(tokens.toEntity())
    }

    override suspend fun replaceTokens(tokens: Tokens) {
        tokensDao.replaceTokens(tokens.toEntity())
    }

    override suspend fun deleteTokens() {
        tokensDao.deleteAllTokens()
    }
}