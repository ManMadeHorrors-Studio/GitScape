package com.manmadehorrorsstudio.gitscape.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manmadehorrorsstudio.gitscape.data.models.ApiResponse
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubRepoListUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubUserUseCase
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.models.RepoListState
import com.manmadehorrorsstudio.gitscape.ui.models.UIEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val getGitHubRepoListUseCase: GetGitHubRepoListUseCase,
    private val getGitHubUserUseCase: GetGitHubUserUseCase,
) : ViewModel() {

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
        if (_userState.value == null)
            loadUser()
        loadRepositories()
    }

    private fun startLoading() {
        _repoListState.update {
            it.copy(
                isLoading = true
            )
        }
    }

    private fun loadRepositories() {
        viewModelScope.launch {
            startLoading()
            val result: ApiResponse<List<GitHubRepo>> = getGitHubRepoListUseCase()
            when (result) {
                is ApiResponse.Success -> {
                    gitScapeLog("loadRepositories result: ${result.data}")
                    _repoListState.update {
                        it.copy(
                            repositories = result.data,
                            isLoading = false
                        )
                    }
                }
                is ApiResponse.Error -> {
                    gitScapeLog("LoadRepositories error", result.exception)
                    _repoListState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    _repoListEvents.send(
                        UIEvents.ShowError(
                            result.exception.message
                        )
                    )
                }
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            val result: ApiResponse<GitHubUser> = getGitHubUserUseCase()
            when (result) {
                is ApiResponse.Success -> {
                    gitScapeLog("loadUser result: ${result.data}")
                    _userState.value =
                        GitHubUser(
                            id = result.data.id,
                            userName = result.data.userName,
                            avatarUrl = result.data.avatarUrl
                        )
                }
                is ApiResponse.Error -> {
                    gitScapeLog("loadUser error", result.exception)
                    _userState.value = null
                }
            }
        }
    }
}