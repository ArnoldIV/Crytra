package com.taras.pet.crytra.crypto.data.mappers

import com.taras.pet.crytra.crypto.data.networking.dto.CoinDto
import com.taras.pet.crytra.crypto.data.networking.dto.CoinPriceDto
import com.taras.pet.crytra.crypto.domain.Coin
import com.taras.pet.crytra.crypto.domain.CoinPrice
import java.time.Instant
import java.time.ZoneId

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

fun CoinPriceDto.toCoinPrice() : CoinPrice{
    return CoinPrice(
        priceUsd = priceUsd,
        dateTime = Instant
            .ofEpochMilli(time)
            .atZone(ZoneId.systemDefault())
    )
}