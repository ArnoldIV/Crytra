package com.taras.pet.crytra.crypto.presentation.coin_list

import com.taras.pet.crytra.core.domain.util.NetworkError

interface CoinListEvent {
    data class Error(val error: NetworkError) : CoinListEvent
}