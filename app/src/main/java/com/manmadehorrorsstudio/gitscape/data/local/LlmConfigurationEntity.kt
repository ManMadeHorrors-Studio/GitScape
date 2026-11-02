package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.manmadehorrorsstudio.gitscape.LLM_CONFIGURATION_TABLE_NAME

@Entity(tableName = LLM_CONFIGURATION_TABLE_NAME)
data class LlmConfigurationEntity(
    @PrimaryKey val id: Int = 0,
    val providerId: String,
    val providerDisplayName: String,
    val aiKey: String? = null,
    val selectedModelId: String? = null,
    val selectedModelProviderId: String? = null,
    val selectedModelProviderDisplayName: String? = null,
    val selectedModelCapabilities: String? = null,
    val selectedModelContextLength: Long? = null,
    val selectedModelMaxOutputTokens: Long? = null
)