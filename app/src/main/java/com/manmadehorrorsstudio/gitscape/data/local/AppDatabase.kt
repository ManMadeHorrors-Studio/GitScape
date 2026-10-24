package com.manmadehorrorsstudio.gitscape.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        SettingsEntity::class,
        GitHubRepoEntity::class,
        AuthTokensEntity::class
    ],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getSettingsDao(): SettingsDao
    abstract fun getGitHubDao(): GitHubDao
    abstract fun getTokensDao(): AuthTokensDao
}