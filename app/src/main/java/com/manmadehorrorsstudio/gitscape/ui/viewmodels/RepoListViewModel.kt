package com.manmadehorrorsstudio.gitscape.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubReadMeUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubRepoListUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubUserUseCase
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.models.RepoListState
import com.manmadehorrorsstudio.gitscape.ui.models.UIEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val getGitHubRepoListUseCase: GetGitHubRepoListUseCase,
    private val getGitHubUserUseCase: GetGitHubUserUseCase,
    private val getGitHubReadMeUseCase: GetGitHubReadMeUseCase
) : ViewModel() {


    fun coso () {
        for (repo in _repoListState.value.repositories)
            callGetReadMeUseCase(repo)


    }

    fun callGetReadMeUseCase(gitHubRepo: GitHubRepo?) {
        gitHubRepo?.let { ghr ->
            runBlocking {
                runCatching { getGitHubReadMeUseCase(gitHubRepo) }
                    .onSuccess { readMe ->
                        gitScapeLog("callGetReadMeUseCase for repo ${gitHubRepo.name}: $readMe")
                    }
                    .onFailure { exception ->
                        gitScapeLog("callGetReadMeUseCase for repo ${gitHubRepo.name} error: $exception")
                    }
            }
        }
    }

    private val _repoListState = MutableStateFlow(RepoListState())
    val repoListstate: StateFlow<RepoListState> = _repoListState.asStateFlow()

    private val _userState = MutableStateFlow<GitHubUser?>(null)
    val userState: StateFlow<GitHubUser?> = _userState.asStateFlow()

    private val _repoListEvents = Channel<UIEvents>(Channel.CONFLATED)
    val repoListEvents = _repoListEvents.receiveAsFlow()

    init {
        refreshRepoListScreen()
    }

    fun refreshRepoListScreen() {
        startLoading()
        viewModelScope.launch {
            val userDeferred = if (_userState.value == null)
                async { loadUser() }
            else
                null
            val repoDeferred = async { loadRepositories() }
            userDeferred?.await()
            repoDeferred.await()
        }
    }

    private fun startLoading() {
        _repoListState.update { it.copy(isLoading = true) }
    }

    private suspend fun loadRepositories() {
        runCatching { getGitHubRepoListUseCase() }
            .onSuccess { repositories ->
                gitScapeLog("loadRepositories result: $repositories")
                _repoListState.update {
                    it.copy(
                        repositories = repositories,
                        isLoading = false
                    )
                }
            }
            .onFailure { exception ->
                gitScapeLog("LoadRepositories error", exception as? Exception)
                _repoListState.update {
                    it.copy(
                        isLoading = false
                    )
                }
                _repoListEvents.send(
                    UIEvents.ShowError(exception.message)
                )
            }
    }

    private suspend fun loadUser() {
        runCatching { getGitHubUserUseCase() }
            .onSuccess { user ->
                gitScapeLog("loadUser result: $user")
                _userState.value = user
            }
            .onFailure { exception ->
                gitScapeLog("loadUser error", exception as? Exception)
                _userState.value = null
            }
    }
}
