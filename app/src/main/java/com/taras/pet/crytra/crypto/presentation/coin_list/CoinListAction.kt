package com.taras.pet.crytra.crypto.presentation.coin_list

import com.taras.pet.crytra.crypto.presentation.CoinUi

sealed interface CoinListAction {
    data class OnCoinClick(val coinUi: CoinUi): CoinListAction
    data object OnRefresh: CoinListAction
}