package com.taras.pet.crytra

import com.taras.pet.crytra.core.domain.util.NetworkError
import com.taras.pet.crytra.crypto.domain.Coin
import com.taras.pet.crytra.crypto.domain.CoinDataSource
import com.taras.pet.crytra.crypto.domain.CoinPrice
import java.time.ZonedDateTime
import com.taras.pet.crytra.core.domain.util.Result

class FakeCoinDataSource : CoinDataSource {

    var coinsToReturn: Result<List<Coin>, NetworkError> = Result.Success(emptyList())
    var historyToReturn: Result<List<CoinPrice>, NetworkError> = Result.Success(emptyList())

    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return coinsToReturn
    }

    override suspend fun getCoinHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        return historyToReturn
    }
}