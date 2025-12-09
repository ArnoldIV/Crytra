package com.taras.pet.crytra.crypto.data.networking.dto

import kotlinx.serialization.Serializable

// API response wrapper.
// API returns { "data": [CoinDto, ...] }
// We read JSON into this DTO type.
@Serializable
data class CoinsResponseDto(
    val data: List<CoinDto>
)
