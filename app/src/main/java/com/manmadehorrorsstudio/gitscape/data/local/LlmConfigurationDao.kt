package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manmadehorrorsstudio.gitscape.LLM_FAVOURITES_TABLE_NAME
import com.manmadehorrorsstudio.gitscape.LLM_CONFIGURATION_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface LlmConfigurationDao {
    @Query("SELECT * FROM $LLM_CONFIGURATION_TABLE_NAME WHERE id = 0")
    fun observeLlmConfiguration(): Flow<LlmConfigurationEntity?>

    @Query("""
        SELECT *
        FROM $LLM_CONFIGURATION_TABLE_NAME
        WHERE id = 0
    """)
    suspend fun getLlmConfiguration(): LlmConfigurationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLlmConfiguration(configuration: LlmConfigurationEntity)

    @Query("DELETE FROM $LLM_CONFIGURATION_TABLE_NAME WHERE id = 0")
    suspend fun clearLlmConfiguration()

    @Query("SELECT * FROM $LLM_FAVOURITES_TABLE_NAME WHERE configurationId = 0")
    fun observeFavouriteList(): Flow<List<LlmFavouriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavourite(favourite: LlmFavouriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavourites(favourites: List<LlmFavouriteEntity>)

    @Query("DELETE FROM $LLM_FAVOURITES_TABLE_NAME WHERE configurationId = 0")
    suspend fun clearFavouriteList()

    @Query("DELETE FROM $LLM_FAVOURITES_TABLE_NAME WHERE configurationId = 0 AND modelId = :modelId")
    suspend fun deleteFavourite(modelId: String)
}