package com.taras.pet.crytra.crypto.data.mappers

import com.taras.pet.crytra.crypto.data.networking.dto.CoinDto
import com.taras.pet.crytra.crypto.domain.Coin

fun CoinDto.toCoin(): Coin {
    return Coin(
        id = id,
        rank = rank,
        name = name,
        symbol = symbol,
        marketCapUsd = marketCapUsd,
        priceUsd = priceUsd,
        percentChange24h = changePercent24Hr
    )
}