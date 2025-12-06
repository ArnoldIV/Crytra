package com.taras.pet.crytra.crypto.domain

import com.taras.pet.crytra.core.domain.util.NetworkError
import com.taras.pet.crytra.core.domain.util.Result

//this describes the DataSource abstraction, but without implementation details.
//domain only knows about Coin (pure model), but not about API or DTO.
interface CoinDataSource {
    suspend fun getCoins(): Result<List<Coin>, NetworkError>
}