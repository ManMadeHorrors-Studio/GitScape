package com.manmadehorrorsstudio.gitscape.data.local

import com.manmadehorrorsstudio.gitscape.data.mappers.mapToDomain
import com.manmadehorrorsstudio.gitscape.data.mappers.toEntity
import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalTokensRepository
import javax.inject.Inject

class LocalTokensRepositoryImpl @Inject constructor(
    private val authTokensDao: AuthTokensDao
) : LocalTokensRepository {

    override suspend fun getTokens(): AuthTokens? {
        return authTokensDao.getTokens()?.mapToDomain()
    }

    override suspend fun saveTokens(authTokens: AuthTokens) {
        authTokensDao.saveTokens(authTokens.toEntity())
    }

    override suspend fun replaceTokens(authTokens: AuthTokens) {
        authTokensDao.replaceTokens(authTokens.toEntity())
    }

    override suspend fun deleteTokens() {
        authTokensDao.deleteAllTokens()
    }
}