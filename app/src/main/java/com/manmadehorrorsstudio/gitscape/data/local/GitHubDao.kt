package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manmadehorrorsstudio.gitscape.GITHUB_REPOSITORIES_TABLE_NAME

@Dao
interface GitHubDao {
    @Query(""" 
        SELECT * 
        FROM $GITHUB_REPOSITORIES_TABLE_NAME
        WHERE :idList IS NULL OR id IN (:idList)
    """)
    suspend fun getRepositories(idList: List<String>? = null): List<GitHubRepoEntity>   //If idList is null, return all repositories

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRepositories(
        repositories: List<GitHubRepoEntity>
    )

    @Query("DELETE FROM $GITHUB_REPOSITORIES_TABLE_NAME")
    suspend fun clearRepositories()
}