package com.manmadehorrorsstudio.gitscape.ui.composables

import android.content.ClipData
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.manmadehorrorsstudio.gitscape.SPACER_HEIGHT_SIZE
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.openBrowser
import com.manmadehorrorsstudio.gitscape.ui.models.AuthState
import com.manmadehorrorsstudio.gitscape.ui.models.AuthStatus
import com.manmadehorrorsstudio.gitscape.ui.models.RepoListState
import com.manmadehorrorsstudio.gitscape.ui.models.UIEvents
import com.manmadehorrorsstudio.gitscape.ui.viewmodels.AgentViewModel
import com.manmadehorrorsstudio.gitscape.ui.viewmodels.AuthViewModel
import com.manmadehorrorsstudio.gitscape.ui.viewmodels.RepoListViewModel
import kotlinx.coroutines.flow.merge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    authViewModel: AuthViewModel,
    repoListViewModel: RepoListViewModel,
    innerPadding: PaddingValues,
    onSettingsClick: () -> Unit
) {
    val repoListState by repoListViewModel.repoListstate.collectAsStateWithLifecycle()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    val clipboard = LocalClipboard.current
    val context = LocalContext.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val errorAlert = remember { mutableStateOf<Pair<Int?, (() -> Unit)?>?>(null) }

    val glowEffectOnScrollEnabled by repoListViewModel.getGlowEffectOnScrollEnabledState().collectAsStateWithLifecycle()

    val pointToRepoInGrid = remember { mutableStateOf<Int?>(null) }

    var showAgent by remember { mutableStateOf(false) }

    val agentViewModel: AgentViewModel = hiltViewModel()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val colors = MaterialTheme.colorScheme

    if (showAgent) {
        agentViewModel.setRepositories(repoListState.repositories)
        ModalBottomSheet(
            containerColor = colors.primary,
            onDismissRequest = { showAgent = false },
            sheetState = sheetState,
            modifier = Modifier
                .padding(horizontal = 15.dp)
        ) {
            AgentChatSheet(
                agentViewModel,
                modifier = Modifier
                    .heightIn(max = screenHeight * 0.7f)
            )
        }
    }

    RepoListScreenInternal(
        repoListState = repoListState,
        authState = authState,
        errorAlert = errorAlert,
        innerPadding = innerPadding,
        dividerColor = colors.primary,
        pointToRepoInGrid = pointToRepoInGrid,
        onAIClick = { showAgent = true },
        onSettingsClick = onSettingsClick,
        onRefreshClick = {
            if (authViewModel.authState.value.authStatus == AuthStatus.OFFLINE)
                authViewModel.collectAccessToken()
            else
                repoListViewModel.refreshRepoListScreen()
        },
        authAdvanceStep = {
            authViewModel.authAdvanceStep()
        },
        glowEffectOnScrollEnabled = glowEffectOnScrollEnabled
    )

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            merge(
                repoListViewModel.repoListEvents,
                agentViewModel.agentEvents,
                authViewModel.authEvents
            ).collect { event ->
                when (event) {
                    is UIEvents.ShowError -> {
                        errorAlert.value = Pair(event.message, event.onAlertConfirm)
                    }
                    is UIEvents.PointToRepo -> {
                        val index = repoListViewModel.repoListstate.value.repositories
                            .indexOfFirst { repo -> repo.id == event.id }

                        if (index in repoListState.repositories.indices) {
                            showAgent = false
                            pointToRepoInGrid.value = index
                        }
                    }
                    is UIEvents.CallOpenBrowser -> {
                        event.stringToCopy?.let { stringToCopy ->
                            clipboard.setClipEntry(
                                ClipEntry(
                                    ClipData.newPlainText(
                                        "Copied_from_GitScape",
                                        stringToCopy
                                    )
                                )
                            )
                        }
                        openBrowser(event.url, context)
                    }

                    is UIEvents.CallRefreshRepo -> {
                        gitScapeLog("CallRefreshRepo")
                        repoListViewModel.refreshRepoListScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun RepoListScreenInternal(
    repoListState: RepoListState,
    authState: AuthState,
    errorAlert: MutableState<Pair<Int?, (() -> Unit)?>?>,
    innerPadding: PaddingValues,
    dividerColor: Color,
    pointToRepoInGrid: MutableState<Int?>,
    onAIClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRefreshClick: () -> Unit,
    authAdvanceStep: () -> Unit,
    glowEffectOnScrollEnabled: Boolean
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
                gitHubUser = authState.gitHubUser
            )

            Spacer(
                modifier = Modifier.height(SPACER_HEIGHT_SIZE)
            )

            HorizontalDivider(
                color = dividerColor
            )

            Spacer(
                modifier = Modifier.height(SPACER_HEIGHT_SIZE)
            )

            RepoListToolbar(
                showAiAndRefreshButtons = authState.authStatus == AuthStatus.VALIDATED || authState.authStatus == AuthStatus.OFFLINE,
                onAIClick = onAIClick,
                onRefreshClick = onRefreshClick,
                onSettingsClick = onSettingsClick
            )

            Spacer(
                modifier = Modifier.height(SPACER_HEIGHT_SIZE)
            )

            HorizontalDivider(
                color = dividerColor
            )

            when {
                repoListState.isLoading || authState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularLoader()
                    }
                }

                authState.authStatus != AuthStatus.VALIDATED && authState.authStatus != AuthStatus.OFFLINE -> {
                    AuthSection(
                        authState = authState,
                        authAdvanceStep = authAdvanceStep
                    )
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
                        pointToRepoInGrid = pointToRepoInGrid,
                        glowEffectOnScrollEnabled = glowEffectOnScrollEnabled
                    )
                }
            }

            AlertMessage(
                errorAlert = errorAlert
            )
        }
    }
}