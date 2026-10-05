package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.GITHUB_GET_REPOS
import com.manmadehorrorsstudio.gitscape.GITHUB_GET_USER
import com.manmadehorrorsstudio.gitscape.MAX_REPOS_NUMBER
import com.manmadehorrorsstudio.gitscape.data.models.GitHubRepoDTO
import com.manmadehorrorsstudio.gitscape.data.models.GitHubUserDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApis {

    @GET(GITHUB_GET_REPOS)
    suspend fun getRepositories(
        @Query("per_page") perPage: Int = MAX_REPOS_NUMBER
    ): List<GitHubRepoDTO>

    @GET(GITHUB_GET_USER)
    suspend fun getUser(): GitHubUserDTO
}