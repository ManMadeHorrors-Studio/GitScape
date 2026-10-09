package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manmadehorrorsstudio.gitscape.SETTINGS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM $SETTINGS_TABLE_NAME WHERE id = 0")
    fun observeSettings(): Flow<SettingsEntity?>

    @Query("""
        SELECT *
        FROM $SETTINGS_TABLE_NAME
        WHERE id = 0
    """)
    suspend fun getSettings(): SettingsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: SettingsEntity)
}