package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manmadehorrorsstudio.gitscape.LLM_CONFIGURATION_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface LlmConfigurationDao {
    @Query("SELECT * FROM $LLM_CONFIGURATION_TABLE_NAME ORDER BY selected DESC, providerId ASC")
    fun observeLlmConfiguration(): Flow<List<LlmConfigurationEntity>>

    @Query("SELECT * FROM $LLM_CONFIGURATION_TABLE_NAME ORDER BY selected DESC, providerId ASC")
    suspend fun getLlmConfigurations(): List<LlmConfigurationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLlmConfiguration(configuration: LlmConfigurationEntity)

    @Query("DELETE FROM $LLM_CONFIGURATION_TABLE_NAME")
    suspend fun clearLlmConfiguration()
}