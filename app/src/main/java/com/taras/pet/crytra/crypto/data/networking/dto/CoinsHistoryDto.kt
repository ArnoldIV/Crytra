package com.taras.pet.crytra.crypto.data.networking.dto

import kotlinx.serialization.Serializable

// API response wrapper.
// API returns { "data": [CoinPriceDto, ...] }
// We read JSON into this DTO type.
@Serializable
data class CoinsHistoryDto(
    val data: List<CoinPriceDto>
)
