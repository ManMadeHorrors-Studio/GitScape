package com.manmadehorrorsstudio.gitscape.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser
import com.manmadehorrorsstudio.gitscape.domain.models.calculateHash
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalGitHubRepository
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubRepoListUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubUserUseCase
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.models.RepoListState
import com.manmadehorrorsstudio.gitscape.ui.models.UIEvents
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
    private val localGitHubRepository: LocalGitHubRepository,
    private val settingsRepository: SettingsRepository
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
        upDateRepoList(emptyList())
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

    private suspend fun loadRepositories() {
        val hashFromLocal = settingsRepository.getRepoListHash()
        runCatching { getGitHubRepoListUseCase() }
            .onSuccess { repositories ->
                gitScapeLog("loadRepositories result: $repositories")
                upDateRepoList(repositories)

                if (settingsRepository.getRepoListEnabled().value) {
                    val hashFromRemote = repositories.calculateHash()
                    if (hashFromLocal != hashFromRemote) {
                        gitScapeLog("LoadRepositories hashFromLocal and hashFromRemote are different")
                        localGitHubRepository.saveGitHubRepositories(repositories)
                        settingsRepository.saveRepoListHash(hashFromRemote)
                    } else
                        gitScapeLog("LoadRepositories hashFromLocal and hashFromRemote are the same")
                }
            }
            .onFailure { exception ->
                gitScapeLog("LoadRepositories error", exception as? Exception)
                _repoListState.update {
                    it.copy(
                        isLoading = false
                    )
                }

                val errorToShow = when {
                    settingsRepository.getRepoListEnabled().value && hashFromLocal != null ->
                        UIEvents.ShowError(
                            message = R.string.REPOLISTVIEW_ERRORS_REPOS_YES_LOAD_FROM_LOCAL,
                            onAlertConfirm = { updateRepoListFromLocal() }
                        )
                    settingsRepository.getRepoListEnabled().value && hashFromLocal == null ->
                        UIEvents.ShowError(
                            message = R.string.REPOLISTVIEW_ERRORS_REPOS_NO_SAVED_LOCAL
                        )
                    else ->
                        UIEvents.ShowError(
                        message = R.string.REPOLISTVIEW_ERRORS_REPOS_NO_LOAD_FROM_LOCAL
                    )
                }
                _repoListEvents.send(errorToShow)
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

    private fun startLoading() {
        _repoListState.update { it.copy(isLoading = true) }
    }

    private fun updateRepoListFromLocal() {
        startLoading()
        viewModelScope.launch {
            try {
                upDateRepoList(localGitHubRepository.getGitHubRepositories())
            } catch (e: Exception) {
                upDateRepoList(emptyList())
                gitScapeLog("updateRepoListFromLocal error", e)
                localGitHubRepository.clearGitHubRepositories()
                settingsRepository.clearRepoList()
                _repoListEvents.send(
                    UIEvents.ShowError(
                        message = R.string.REPOLISTVIEW_ERRORS_REPOS_CORRUPTED_LOCAL
                    )
                )
            }
        }
    }

    private fun upDateRepoList(repoList: List<GitHubRepo>) {
        _repoListState.update {
            it.copy(
                repositories = repoList,
                isLoading = false
            )
        }
    }
}
