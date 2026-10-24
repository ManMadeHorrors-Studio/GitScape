package com.manmadehorrorsstudio.gitscape.data.local

import com.manmadehorrorsstudio.gitscape.data.mappers.toDomain
import com.manmadehorrorsstudio.gitscape.data.mappers.toEntity
import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalTokensRepository
import javax.inject.Inject

class LocalTokensRepositoryImpl @Inject constructor(
    private val tokensDao: TokensDao
) : LocalTokensRepository {

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