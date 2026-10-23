package com.manmadehorrorsstudio.gitscape.di

import com.manmadehorrorsstudio.gitscape.GITHUB_API_BASE_PATH
import com.manmadehorrorsstudio.gitscape.GITHUB_BASE_PATH
import com.manmadehorrorsstudio.gitscape.data.remote.AuthApis
import com.manmadehorrorsstudio.gitscape.data.remote.GitHubApis
import com.manmadehorrorsstudio.gitscape.data.remote.GitHubAuthInterceptor
import com.manmadehorrorsstudio.gitscape.data.remote.loggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGitHubAuthInterceptor(): GitHubAuthInterceptor {
        return GitHubAuthInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        githubAuthInterceptor: GitHubAuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(githubAuthInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGitHubApi(
        okHttpClient: OkHttpClient
    ): GitHubApis {
        return Retrofit.Builder()
            .baseUrl("https://$GITHUB_API_BASE_PATH/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(GitHubApis::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        okHttpClient: OkHttpClient
    ): AuthApis {
        return Retrofit.Builder()
            .baseUrl("https://$GITHUB_BASE_PATH/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AuthApis::class.java)
    }
}