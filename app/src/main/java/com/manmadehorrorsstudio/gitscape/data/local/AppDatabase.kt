package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        SettingsEntity::class,
        GitHubRepoEntity::class,
        AuthTokensEntity::class,
        LlmConfigurationEntity::class
    ],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getSettingsDao(): SettingsDao
    abstract fun getGitHubDao(): GitHubDao
    abstract fun getTokensDao(): AuthTokensDao
    abstract fun getLlmConfigurationDao(): LlmConfigurationDao
}