package com.taras.pet.crytra.crypto.data.networking


import com.taras.pet.crytra.core.data.networking.constructUrl
import com.taras.pet.crytra.core.data.networking.safeCall
import com.taras.pet.crytra.core.domain.util.NetworkError
import com.taras.pet.crytra.core.domain.util.Result
import com.taras.pet.crytra.core.domain.util.map
import com.taras.pet.crytra.crypto.data.mappers.toCoin
import com.taras.pet.crytra.crypto.data.mappers.toCoinPrice
import com.taras.pet.crytra.crypto.data.networking.dto.CoinsHistoryDto
import com.taras.pet.crytra.crypto.data.networking.dto.CoinsResponseDto
import com.taras.pet.crytra.crypto.domain.Coin
import com.taras.pet.crytra.crypto.domain.CoinDataSource
import com.taras.pet.crytra.crypto.domain.CoinPrice
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.ZoneId
import java.time.ZonedDateTime

class RemoteCoinDataSource(
    private val httpClient: HttpClient
) : CoinDataSource {
    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        // safeCall making api request
        return safeCall<CoinsResponseDto> {
            httpClient.get(
                urlString = constructUrl("/assets")
            )
            // mapping DTO -> domain-model
        }.map { response ->
            response.data.map { it.toCoin() }
        }
    }

    override suspend fun getCoinHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        val startMillis = start.withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli()
        val endMillis = end.withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli()
        return safeCall<CoinsHistoryDto> {
            httpClient.get(
                urlString = constructUrl("/assets/$coinId/history")
            ) {
                //can be changed in the future to give a user the option to choose an interval
                parameter("interval", "h6")
                parameter("start",startMillis)
                parameter("end", endMillis)
            }
        }.map { response ->
            response.data.map { it.toCoinPrice() }
        }
    }
}