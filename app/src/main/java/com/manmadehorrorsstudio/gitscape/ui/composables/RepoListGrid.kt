package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.manmadehorrorsstudio.gitscape.SPACER_HEIGHT_SIZE
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo

@Composable
fun RepoListGrid(
    gitHubRepos: List<GitHubRepo>,
    pointToRepoInGrid: MutableState<Int?>,
    glowEffectOnScrollEnabled: Boolean
) {
    val gridState = rememberLazyGridState()

    var forcedAnimationIndex by remember {
        mutableStateOf<Int?>(null)
    }
    var forcedAnimationTrigger by remember {
        mutableIntStateOf(0)
    }
    var isProgrammaticScroll by remember {
        mutableStateOf(false)
    }
    val fullyVisibleItemIndexes by remember {
        derivedStateOf {
            if(glowEffectOnScrollEnabled) {
                val layoutInfo = gridState.layoutInfo

                layoutInfo.visibleItemsInfo
                    .filter { item ->
                        val itemTop = item.offset.y
                        val itemBottom = itemTop + item.size.height

                        itemTop >= layoutInfo.viewportStartOffset &&
                                itemBottom <= layoutInfo.viewportEndOffset
                    }
                    .map { it.index }
                    .toSet()
            } else
                emptySet()
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(
            top = SPACER_HEIGHT_SIZE
        )
    ) {
        itemsIndexed(
            items = gitHubRepos,
            key = { _, gitHubRepo ->
                gitHubRepo.id
            }
        ) { index, gitHubRepo ->
            RepoCard(
                gitHubRepo = gitHubRepo,
                isFullyVisible = index in fullyVisibleItemIndexes,
                forcedAnimationTrigger =
                    if (forcedAnimationIndex == index) {
                        forcedAnimationTrigger
                    } else {
                        null
                    },
                ignoreVisibilityAnimation = isProgrammaticScroll
            )
        }
    }

    LaunchedEffect(pointToRepoInGrid.value) {
        pointToRepoInGrid.value?.let { index ->
            forcedAnimationIndex = index
            forcedAnimationTrigger++
            isProgrammaticScroll = true
            gridState.animateScrollToItem(index)
            isProgrammaticScroll = false
            pointToRepoInGrid.value = null
        }
    }
}