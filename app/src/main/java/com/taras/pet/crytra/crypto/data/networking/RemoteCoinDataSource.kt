package com.taras.pet.crytra.crypto.data.networking

import com.taras.pet.crytra.core.data.networking.constructUrl
import com.taras.pet.crytra.core.data.networking.safeCall
import com.taras.pet.crytra.core.domain.util.NetworkError
import com.taras.pet.crytra.core.domain.util.Result
import com.taras.pet.crytra.core.domain.util.map
import com.taras.pet.crytra.crypto.data.mappers.toCoin
import com.taras.pet.crytra.crypto.data.networking.dto.CoinsResponseDto
import com.taras.pet.crytra.crypto.domain.Coin
import com.taras.pet.crytra.crypto.domain.CoinDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class RemoteCoinDataSource(
    private val httpClient: HttpClient
): CoinDataSource {
    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        // safeCall making api request
        return safeCall<CoinsResponseDto> {
            httpClient.get (
                urlString = constructUrl("/assets")
            )
            // mapping DTO -> domain-model
        }.map { response ->
            response.data.map { it.toCoin() }
        }
    }
}