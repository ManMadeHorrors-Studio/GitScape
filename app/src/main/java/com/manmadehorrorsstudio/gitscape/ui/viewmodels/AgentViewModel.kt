package com.manmadehorrorsstudio.gitscape.ui.viewmodels

import ai.koog.prompt.message.Message
import ai.koog.prompt.message.MessagePart
import ai.koog.prompt.message.RequestMetaInfo
import ai.koog.prompt.message.ResponseMetaInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manmadehorrorsstudio.gitscape.domain.repositories.AgentRepository
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.ai.AgentUiCommand
import com.manmadehorrorsstudio.gitscape.ui.models.AgentState
import com.manmadehorrorsstudio.gitscape.ui.models.UIEvents
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Clock

@HiltViewModel
class AgentViewModel @Inject constructor(
    private val agentRepository: AgentRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _agentState = MutableStateFlow(AgentState())
    val agentState: StateFlow<AgentState> = _agentState.asStateFlow()

    private val _agentEvents = Channel<UIEvents>(Channel.CONFLATED)
    val agentEvents = _agentEvents.receiveAsFlow()

    private var repos: List<GitHubRepo> = emptyList()

    fun getCurrentLanguageTag() = settingsRepository.getLanguage().value.locale.toLanguageTag()
    fun setRepositories(value: List<GitHubRepo>) { repos = value.toList() }
    fun send(prompt: String) {
        if (prompt.isNotBlank() && !_agentState.value.isLoading) {
            _agentState.update { agst ->
                agst.copy(
                    messages = agst.messages + Message.User(
                        parts = listOf(
                            MessagePart.Text(prompt.trim())
                        ),
                        metaInfo = RequestMetaInfo(
                            timestamp = Clock.System.now()
                        )
                    ),
                    isLoading = true,
                    error = null
                )
            }
            viewModelScope.launch {
                runCatching {
                    agentRepository.ask(
                        messages = _agentState.value.messages,
                        repositories = repos,
                        onUiCommand = { command ->
                            when (command) {
                                is AgentUiCommand.PointToRepo -> {
                                    gitScapeLog("gridState tool viewModel: $command.repoId")
                                    _agentEvents.trySend(UIEvents.PontToRepo(command.repoId))
                                }
                            }
                        }
                    )
                }
                    .onSuccess { answer ->
                        _agentState.update { agst ->
                            agst.copy(
                                messages = agst.messages + Message.Assistant(
                                    parts = listOf(
                                        MessagePart.Text(answer.trim())
                                    ),
                                    metaInfo = ResponseMetaInfo(
                                        timestamp = Clock.System.now()
                                    )
                                ),
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
