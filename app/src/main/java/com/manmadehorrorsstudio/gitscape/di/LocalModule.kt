package com.manmadehorrorsstudio.gitscape.di

import android.content.Context
import androidx.room.Room
import com.manmadehorrorsstudio.gitscape.AppSettings
import com.manmadehorrorsstudio.gitscape.GITSCAPE_DATABASE
import com.manmadehorrorsstudio.gitscape.data.local.AppDatabase
import com.manmadehorrorsstudio.gitscape.data.local.GitHubDao
import com.manmadehorrorsstudio.gitscape.data.local.LocalGitHubRepositoryImpl
import com.manmadehorrorsstudio.gitscape.data.local.LocalTokensRepositoryImpl
import com.manmadehorrorsstudio.gitscape.data.local.SettingsDao
import com.manmadehorrorsstudio.gitscape.data.local.SettingsRepositoryImpl
import com.manmadehorrorsstudio.gitscape.data.local.AuthTokensDao
import com.manmadehorrorsstudio.gitscape.data.local.migrationsList
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalGitHubRepository
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalTokensRepository
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppSettingsEntryPoint {
    fun appSettings(): AppSettings
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            GITSCAPE_DATABASE
        )
            .addMigrations(*migrationsList)
            .build()
    }

    @Provides
    fun provideSettingsDao(
        database: AppDatabase
    ): SettingsDao {
        return database.getSettingsDao()
    }

    @Provides
    fun provideGitHubDao(
        database: AppDatabase
    ): GitHubDao {
        return database.getGitHubDao()
    }

    @Provides
    fun provideTokensDao(
        database: AppDatabase
    ): AuthTokensDao {
        return database.getTokensDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        implementation: SettingsRepositoryImpl
    ): SettingsRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalGitHubModule {

    @Binds
    @Singleton
    abstract fun bindLocalGitHubRepository(
        implementation: LocalGitHubRepositoryImpl
    ): LocalGitHubRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalTokensModule {

    @Binds
    @Singleton
    abstract fun bindLocalTokensRepository(
        implementation: LocalTokensRepositoryImpl
    ): LocalTokensRepository
}