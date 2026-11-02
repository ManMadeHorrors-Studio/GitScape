package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import com.manmadehorrorsstudio.gitscape.LLM_FAVOURITES_TABLE_NAME

@Entity(
    tableName = LLM_FAVOURITES_TABLE_NAME,
    primaryKeys = ["configurationId", "modelId"],
    foreignKeys = [
        ForeignKey(
            entity = LlmConfigurationEntity::class,
            parentColumns = ["id"],
            childColumns = ["configurationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LlmFavouriteEntity(
    val configurationId: Int = 0,
    val modelId: String
)