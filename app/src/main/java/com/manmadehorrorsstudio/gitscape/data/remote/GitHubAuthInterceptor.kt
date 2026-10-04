package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.BuildConfig
import com.manmadehorrorsstudio.gitscape.GITHUB_BASE_PATH
import okhttp3.Interceptor
import okhttp3.Response

class GitHubAuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        return if (request.url.host.equals(GITHUB_BASE_PATH, ignoreCase = true)) {

            val authenticatedRequest = request.newBuilder()
                .header(
                    "Authorization",
                    "Bearer ${BuildConfig.GITHUB_PAT}"
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