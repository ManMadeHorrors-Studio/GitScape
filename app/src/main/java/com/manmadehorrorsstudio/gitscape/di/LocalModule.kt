package com.manmadehorrorsstudio.gitscape.di

import android.content.Context
import androidx.room.Room
import com.manmadehorrorsstudio.gitscape.AppSettings
import com.manmadehorrorsstudio.gitscape.GITSCAPE_DATABASE
import com.manmadehorrorsstudio.gitscape.data.local.AppDatabase
import com.manmadehorrorsstudio.gitscape.data.local.SettingsDao
import com.manmadehorrorsstudio.gitscape.data.local.SettingsRepositoryImpl
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
            .build()
    }

    @Provides
    fun provideSettingsDao(
        database: AppDatabase
    ): SettingsDao {
        return database.settingsDao()
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