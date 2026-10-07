package com.manmadehorrorsstudio.gitscape.di

import com.manmadehorrorsstudio.gitscape.data.remote.GitHubApis
import com.manmadehorrorsstudio.gitscape.data.remote.GitHubRepositoryImpl
import com.manmadehorrorsstudio.gitscape.domain.ai.AgentRepository
import com.manmadehorrorsstudio.gitscape.domain.ai.AgentRepositoryImpl
import com.manmadehorrorsstudio.gitscape.domain.repositories.GitHubRepository
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubRepoListUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubUserUseCase
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
        api: GitHubApis
    ): GitHubRepository {
        return GitHubRepositoryImpl(api)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AgentModule {
    @Provides
    @Singleton
    fun provideAgentRepository(): AgentRepository =
        AgentRepositoryImpl()
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
}