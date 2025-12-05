package com.taras.pet.crytra.crypto.presentation.coin_list

import androidx.compose.runtime.Immutable
import com.taras.pet.crytra.crypto.presentation.CoinUi

@Immutable
data class CoinListState(
    val isLoading: Boolean = false,
    val coins: List<CoinUi> = emptyList(),
    val selectedCoin: CoinUi? = null,
)
