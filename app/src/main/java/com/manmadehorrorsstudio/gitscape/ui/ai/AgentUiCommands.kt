package com.manmadehorrorsstudio.gitscape.ui.ai

sealed interface AgentUiCommand {

    data class PointToRepo(
        val repoId: String
    ) : AgentUiCommand
}