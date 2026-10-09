package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.manmadehorrorsstudio.gitscape.SETTINGS_TABLE_NAME

@Entity(tableName = SETTINGS_TABLE_NAME)
data class SettingsEntity(
    @PrimaryKey val id: Int = 0,
    val language: String? = null,
    val repoListEnabled: Boolean = true
)