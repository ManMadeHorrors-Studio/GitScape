package com.manmadehorrorsstudio.gitscape.di

import com.manmadehorrorsstudio.gitscape.AppSettings
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppSettingsEntryPoint {
    fun appSettings(): AppSettings
}