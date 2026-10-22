package com.manmadehorrorsstudio.gitscape.di

import ai.koog.http.client.ktor.KtorKoogHttpClient
import ai.koog.prompt.executor.clients.google.GoogleLLMClient
import ai.koog.prompt.executor.llms.MultiLLMPromptExecutor
import ai.koog.prompt.executor.model.PromptExecutor
import com.manmadehorrorsstudio.gitscape.BuildConfig
import com.manmadehorrorsstudio.gitscape.data.remote.GitHubApis
import com.manmadehorrorsstudio.gitscape.data.remote.GitHubRepositoryImpl
import com.manmadehorrorsstudio.gitscape.domain.repositories.AgentRepository
import com.manmadehorrorsstudio.gitscape.data.ai.AgentRepositoryImpl
import com.manmadehorrorsstudio.gitscape.data.ai.GitHubReadMeCache
import com.manmadehorrorsstudio.gitscape.domain.repositories.GitHubRepository
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubReadMeUseCase
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
object AgentModule {

    @Provides
    @Singleton
    fun provideAiKey(): String = BuildConfig.AI_KEY.trim()

    @Provides
    @Singleton
    fun provideMultiLLMPromptExecutor(
        aiKey: String
    ): MultiLLMPromptExecutor {

        return MultiLLMPromptExecutor(
            // Gemini
            GoogleLLMClient(
                apiKey = aiKey,
                httpClientFactory = KtorKoogHttpClient.Factory()
            )/*,
            Uncomment the implementation for the LLM provider you want to use
            // OpenAI
            OpenAILLMClient(
                apiKey = aiKey,
                httpClientFactory = KtorKoogHttpClient.Factory()
            ),
            // Claude
            AnthropicLLMClient(
                apiKey = aiKey,
                httpClientFactory = KtorKoogHttpClient.Factory()
            ),
            // DeepSeek
            DeepSeekLLMClient(
                apiKey = aiKey,
                httpClientFactory = httpFactory
            ),
            // OpenRouter
            OpenRouterLLMClient(
                apiKey = aiKey,
                httpClientFactory = httpFactory
            ),
            // Mistral
            MistralAILLMClient(
                apiKey = aiKey,
                httpClientFactory = httpFactory
            ),
            //DashScope Qwen
            DashscopeLLMClient(
                apiKey = aiKey,
                httpClientFactory = httpFactory
            ),
            //Ollama
            OllamaClient(
                baseUrl = URL_SERVER,
                httpClientFactory = httpFactory
            )*/
        )
    }

    @Provides
    @Singleton
    fun providePromptExecutor(
        multiLLMPromptExecutor: MultiLLMPromptExecutor
    ): PromptExecutor = multiLLMPromptExecutor

    @Provides
    @Singleton
    fun provideAgentRepository(
        promptExecutor: PromptExecutor,
        aiKey: String
    ): AgentRepository =
        AgentRepositoryImpl(
            promptExecutor = promptExecutor,
            aiKey = aiKey
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
}