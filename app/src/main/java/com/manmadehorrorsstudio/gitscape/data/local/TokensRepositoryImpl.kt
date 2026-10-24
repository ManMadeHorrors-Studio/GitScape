package com.manmadehorrorsstudio.gitscape.data.local

import com.manmadehorrorsstudio.gitscape.data.mappers.toDomain
import com.manmadehorrorsstudio.gitscape.data.mappers.toEntity
import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens
import com.manmadehorrorsstudio.gitscape.domain.repositories.TokensRepository
import javax.inject.Inject

class TokensRepositoryImpl @Inject constructor(
    private val tokensDao: TokensDao
) : TokensRepository {

    override suspend fun getTokens(): AuthTokens? {
        return tokensDao.getTokens()?.toDomain()
    }

    override suspend fun saveTokens(authTokens: AuthTokens) {
        tokensDao.saveTokens(authTokens.toEntity())
    }

    override suspend fun replaceTokens(authTokens: AuthTokens) {
        tokensDao.replaceTokens(authTokens.toEntity())
    }

    override suspend fun deleteTokens() {
        tokensDao.deleteAllTokens()
    }
}