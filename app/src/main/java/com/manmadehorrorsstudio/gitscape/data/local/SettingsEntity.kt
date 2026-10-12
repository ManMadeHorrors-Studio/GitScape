package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.manmadehorrorsstudio.gitscape.SETTINGS_TABLE_NAME
import com.manmadehorrorsstudio.gitscape.ui.models.ColorStyle

@Entity(tableName = SETTINGS_TABLE_NAME)
data class SettingsEntity(
    @PrimaryKey val id: Int = 0,
    val language: String? = null,
    val repoListEnabled: Boolean = true,
    val repoListHash: String? = null,
    val colorStyle: ColorStyle = ColorStyle.AZURE_STYLE
)