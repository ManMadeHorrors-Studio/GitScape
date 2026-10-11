package com.manmadehorrorsstudio.gitscape.data.ai

import com.manmadehorrorsstudio.gitscape.domain.models.GitHubReadMe
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import java.lang.ref.SoftReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubReadMeCache @Inject constructor() {
    private val cache = mutableMapOf<String, SoftReference<GitHubReadMe>>()

    suspend fun getOrLoad(
        repoId: String,
        loader: suspend () -> GitHubReadMe
    ): GitHubReadMe {
        cache[repoId]?.get()?.let { readMe ->
            gitScapeLog("GitHubReadMeCache recovered from cache for $repoId")
            return readMe
        }

        val readMe = loader()
        cache[repoId] = SoftReference(readMe)

        return readMe
    }
}