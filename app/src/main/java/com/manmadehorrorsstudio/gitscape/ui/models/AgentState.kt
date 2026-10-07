package com.manmadehorrorsstudio.gitscape.ui.models

data class AgentState(
    val messages: List<AgentMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)