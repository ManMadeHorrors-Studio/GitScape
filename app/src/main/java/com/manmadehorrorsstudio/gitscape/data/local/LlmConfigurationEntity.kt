package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.manmadehorrorsstudio.gitscape.LLM_CONFIGURATION_TABLE_NAME

@Entity(tableName = LLM_CONFIGURATION_TABLE_NAME)
data class LlmConfigurationEntity(
    @PrimaryKey val providerId: String,
    val aiKey: String? = null,
    val selectedModelId: String? = null,
    val selected: Boolean = false,
)