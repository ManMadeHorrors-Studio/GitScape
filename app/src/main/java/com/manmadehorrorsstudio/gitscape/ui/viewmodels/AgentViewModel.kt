package com.manmadehorrorsstudio.gitscape.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manmadehorrorsstudio.gitscape.domain.ai.AgentRepository
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.ui.models.AgentMessage
import com.manmadehorrorsstudio.gitscape.ui.models.AgentState
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentViewModel @Inject constructor(
    private val agentRepository: AgentRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _agentState = MutableStateFlow(AgentState())
    val agentState: StateFlow<AgentState> = _agentState.asStateFlow()
    private var repos: List<GitHubRepo> = emptyList()

    fun getCurrentLanguageTag() = settingsRepository.getLanguage().value.locale.toLanguageTag()
    fun setRepositories(value: List<GitHubRepo>) { repos = value.toList() }
    fun send(prompt: String) {
        if (prompt.isNotBlank() && !_agentState.value.isLoading) {
            _agentState.update {
                it.copy(
                    messages = it.messages + AgentMessage(true, prompt.trim()),
                    isLoading = true,
                    error = null
                )
            }
            viewModelScope.launch {
                runCatching {
                    agentRepository.ask(prompt, repos)
                }
                    .onSuccess { answer ->
                        _agentState.update {
                            it.copy(
                                messages = it.messages + AgentMessage(false, answer),
                                isLoading = false
                            )
                        }
                    }
                    .onFailure { error ->
                        _agentState.update {
                            it.copy(
                                isLoading = false,
                                error = error.message ?: "Unknown AI error"
                            )
                        }
                    }
            }
        }
    }
}
