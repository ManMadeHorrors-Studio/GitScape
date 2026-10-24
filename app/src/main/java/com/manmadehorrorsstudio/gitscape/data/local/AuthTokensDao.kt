package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.manmadehorrorsstudio.gitscape.TOKENS_TABLE_NAME

@Dao
interface AuthTokensDao {

    @Query("SELECT * FROM $TOKENS_TABLE_NAME LIMIT 1")
    suspend fun getTokens(): AuthTokensEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTokens(tokens: AuthTokensEntity)

    @Query("DELETE FROM $TOKENS_TABLE_NAME")
    suspend fun deleteAllTokens()

    @Transaction
    suspend fun replaceTokens(tokens: AuthTokensEntity) {
        deleteAllTokens()
        saveTokens(tokens)
    }

    /* Multiple users are not actually supported
    @Query("SELECT * FROM $TOKENS_TABLE_NAME WHERE githubUserId = :githubUserId")
    suspend fun getTokensByUserId(
        githubUserId: String
    ): AuthTokensEntity?

    @Query("DELETE FROM $TOKENS_TABLE_NAME WHERE githubUserId = :githubUserId")
    suspend fun deleteTokensByUserId(
        githubUserId: String
    )*/
}