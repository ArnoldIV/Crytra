package com.taras.pet.crytra.crypto.data.networking.dto

import kotlinx.serialization.Serializable

//raw data, depends on the API structure → may change at any time.
@Serializable
data class CoinDto(
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val marketCapUsd: Double,
    val priceUsd: Double,
    val changePercent24Hr: Double
)
