package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.GITHUB_API_BASE_PATH
import com.manmadehorrorsstudio.gitscape.data.local.AuthCache
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class GitHubAuthInterceptor @Inject constructor(
    private val authCache: AuthCache
)  : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        return if (request.url.host.equals(GITHUB_API_BASE_PATH, ignoreCase = true)) {

            val authenticatedRequest = request.newBuilder()
                .header(
                    "Authorization",
                    "Bearer ${authCache.getAccessToken()}"
                )
                .header(
                    "Accept",
                    "application/vnd.github+json"
                )
                .header(
                    "X-GitHub-Api-Version",
                    "2026-03-10"
                )
                .build()

            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(request)
        }
    }
}