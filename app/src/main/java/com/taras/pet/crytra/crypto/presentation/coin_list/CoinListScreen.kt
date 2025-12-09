package com.taras.pet.crytra.crypto.presentation.coin_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.taras.pet.crytra.crypto.presentation.coin_list.components.CoinListItem
import com.taras.pet.crytra.crypto.presentation.coin_list.components.PullToRefreshBox
import com.taras.pet.crytra.crypto.presentation.coin_list.components.previewCoin
import com.taras.pet.crytra.ui.theme.CrytraTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListScreen(
    state: CoinListState,
    onAction: (CoinListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val animateList = remember { mutableStateOf(false) }

    LaunchedEffect(state.isInitialLoading) {
        if (!state.isInitialLoading) {
            animateList.value = true
        }
    }

    PullToRefreshBox(
        isRefreshing = state.isLoading && !state.isInitialLoading,
        onRefresh = { onAction(CoinListAction.OnRefresh) },
        modifier = modifier.fillMaxSize()
    ) {

        if (state.isInitialLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@PullToRefreshBox
        }

        AnimatedVisibility(
            visible = animateList.value,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.coins) { coinUi ->
                    CoinListItem(
                        coinUi = coinUi,
                        onClick = { onAction(CoinListAction.OnCoinClick(coinUi)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CoinListScreenPreview() {
    CrytraTheme {
        CoinListScreen(
            state = CoinListState(
                coins = (1..100).map {
                    previewCoin.copy(id = it.toString())
                }
            ),
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            onAction = {}
        )
    }
}