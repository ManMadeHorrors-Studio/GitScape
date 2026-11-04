package com.manmadehorrorsstudio.gitscape.di

import com.manmadehorrorsstudio.gitscape.data.remote.GitHubApis
import com.manmadehorrorsstudio.gitscape.data.remote.GitHubRepositoryImpl
import com.manmadehorrorsstudio.gitscape.domain.repositories.AgentRepository
import com.manmadehorrorsstudio.gitscape.data.ai.AgentRepositoryImpl
import com.manmadehorrorsstudio.gitscape.data.ai.GitHubReadMeCache
import com.manmadehorrorsstudio.gitscape.data.ai.LlmCache
import com.manmadehorrorsstudio.gitscape.data.remote.AuthApis
import com.manmadehorrorsstudio.gitscape.data.remote.AuthRepositoryImpl
import com.manmadehorrorsstudio.gitscape.domain.repositories.AuthRepository
import com.manmadehorrorsstudio.gitscape.domain.repositories.GitHubRepository
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetAuthDeviceUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetAuthTokensUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubReadMeUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubRepoListUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubUserUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.RefreshAuthTokensUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGitHubRepository(
        api: GitHubApis,
        readMeCache: GitHubReadMeCache
    ): GitHubRepository {
        return GitHubRepositoryImpl(
            api,
            readMeCache
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApis
    ): AuthRepository {
        return AuthRepositoryImpl(
            api
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
object LlmCacheModule {

    @Provides
    @Singleton
    fun provideLlmCache(): LlmCache {
        return LlmCache()
    }
}

    @Module
@InstallIn(SingletonComponent::class)
object AgentModule {

    @Provides
    @Singleton
    fun provideAgentRepository(
        llmCache: LlmCache
    ): AgentRepository =
        AgentRepositoryImpl(
            llmCache = llmCache
        )
}

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetGitHubRepoListUseCase(
        repository: GitHubRepository
    ): GetGitHubRepoListUseCase {
        return GetGitHubRepoListUseCase(repository)
    }

    @Provides
    fun provideGetGitHubUserUseCase(
        repository: GitHubRepository
    ): GetGitHubUserUseCase {
        return GetGitHubUserUseCase(repository)
    }

    @Provides
    fun provideGetGitHubReadMeUseCase(
        repository: GitHubRepository
    ): GetGitHubReadMeUseCase {
        return GetGitHubReadMeUseCase(repository)
    }

    @Provides
    fun provideGetAuthDeviceUseCase(
        repository: AuthRepository
    ): GetAuthDeviceUseCase {
        return GetAuthDeviceUseCase(repository)
    }

    @Provides
    fun provideGetAuthTokensUseCase(
        repository: AuthRepository
    ): GetAuthTokensUseCase {
        return GetAuthTokensUseCase(repository)
    }

    @Provides
    fun provideRefreshAuthTokensUseCase(
        repository: AuthRepository
    ): RefreshAuthTokensUseCase {
        return RefreshAuthTokensUseCase(repository)
    }
}