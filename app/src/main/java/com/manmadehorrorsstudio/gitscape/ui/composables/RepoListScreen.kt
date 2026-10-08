package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.manmadehorrorsstudio.gitscape.SPACER_HEIGHT_SIZE
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser
import com.manmadehorrorsstudio.gitscape.ui.models.RepoListState
import com.manmadehorrorsstudio.gitscape.ui.models.UIEvents
import com.manmadehorrorsstudio.gitscape.ui.theme.*
import com.manmadehorrorsstudio.gitscape.ui.viewmodels.AgentViewModel
import com.manmadehorrorsstudio.gitscape.ui.viewmodels.RepoListViewModel
import kotlinx.coroutines.flow.merge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListScreen(
    repoListViewModel: RepoListViewModel,
    innerPadding: PaddingValues,
    onSettingsClick: () -> Unit
) {
    val repoListState by repoListViewModel.repoListstate.collectAsStateWithLifecycle()
    val userState by repoListViewModel.userState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    val errorMessage = remember { mutableStateOf<String?>(null) }

    var pointToRepoInGrid = remember { mutableStateOf<Int?>(null) }

    var showAgent by remember { mutableStateOf(false) }

    val agentViewModel: AgentViewModel = hiltViewModel()

    if (showAgent) {
        agentViewModel.setRepositories(repoListState.repositories)
        ModalBottomSheet(
            containerColor = StrongAzure,
            onDismissRequest = { showAgent = false },
            modifier = Modifier
                .padding(horizontal = 15.dp)
        ) {
            AgentChatSheet(agentViewModel)
        }
    }

    RepoListScreenInternal(
        repoListState = repoListState,
        userState = userState,
        errorMessage = errorMessage,
        innerPadding = innerPadding,
        pointToRepoInGrid = pointToRepoInGrid,
        onAIClick = { showAgent = true },
        onSettingsClick = onSettingsClick,
        onRefreshClick = { repoListViewModel.refreshRepoListScreen() }
    )

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            merge(
                repoListViewModel.repoListEvents,
                agentViewModel.agentEvents
            ).collect { event ->
                when (event) {
                    is UIEvents.ShowError -> {
                        errorMessage.value = event.message
                    }
                    is UIEvents.PontToRepo -> { event.id
                        val index = repoListViewModel.repoListstate.value.repositories
                            .indexOfFirst { repo -> repo.id == event.id }

                        if (index in repoListState.repositories.indices) {
                            showAgent = false
                            pointToRepoInGrid.value = index
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RepoListScreenInternal(
    repoListState: RepoListState,
    userState: GitHubUser?,
    errorMessage: MutableState<String?>,
    innerPadding: PaddingValues,
    pointToRepoInGrid: MutableState<Int?>,
    onAIClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            RepoListHeader(
                gitHubUser = userState
            )

            Spacer(
                modifier = Modifier.height(SPACER_HEIGHT_SIZE)
            )

            HorizontalDivider(
                color = StrongAzure
            )

            Spacer(
                modifier = Modifier.height(SPACER_HEIGHT_SIZE)
            )


            RepoListToolbar(
                onAIClick = onAIClick,
                onRefreshClick = onRefreshClick,
                onSettingsClick = onSettingsClick
            )

            Spacer(
                modifier = Modifier.height(SPACER_HEIGHT_SIZE)
            )

            HorizontalDivider(
                color = StrongAzure
            )

            when {
                repoListState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularLoader()
                    }
                }

                repoListState.repositories.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyListFiller()
                    }
                }

                else -> {
                    RepoListGrid(
                        gitHubRepos = repoListState.repositories,
                        pointToRepoInGrid = pointToRepoInGrid
                    )
                }
            }

            AlertMessage(
                alertMessage = errorMessage
            )
        }
    }
}