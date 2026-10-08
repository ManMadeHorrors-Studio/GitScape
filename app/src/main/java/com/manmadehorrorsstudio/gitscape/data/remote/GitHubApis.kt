package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.GITHUB_GET_README
import com.manmadehorrorsstudio.gitscape.GITHUB_GET_REPOS_PATH
import com.manmadehorrorsstudio.gitscape.GITHUB_GET_USER_PATH
import com.manmadehorrorsstudio.gitscape.GITHUB_OWNER
import com.manmadehorrorsstudio.gitscape.GITHUB_REPO
import com.manmadehorrorsstudio.gitscape.MAX_REPOS_NUMBER
import com.manmadehorrorsstudio.gitscape.data.models.GitHubReadMeDTO
import com.manmadehorrorsstudio.gitscape.data.models.GitHubRepoDTO
import com.manmadehorrorsstudio.gitscape.data.models.GitHubUserDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApis {

    @GET(GITHUB_GET_REPOS_PATH)
    suspend fun getRepositories(
        @Query("per_page") perPage: Int = MAX_REPOS_NUMBER
    ): List<GitHubRepoDTO>

    @GET(GITHUB_GET_USER_PATH)
    suspend fun getUser(): GitHubUserDTO

    @GET(GITHUB_GET_README)
    suspend fun getGitHubReadMe(
        @Path(GITHUB_OWNER) owner: String,
        @Path(GITHUB_REPO) repo: String
    ): GitHubReadMeDTO
}