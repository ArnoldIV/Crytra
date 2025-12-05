package com.taras.pet.crytra.crypto.presentation

import android.icu.text.NumberFormat
import androidx.annotation.DrawableRes
import com.taras.pet.crytra.crypto.domain.Coin
import com.taras.pet.crytra.core.presentation.util.getDrawableIdForCoin
import java.util.Locale

data class CoinUi(
    val id: String,
    val name: String,
    val rank: Int,
    val symbol: String,
    val priceUsd: DisplayableNumber,
    val marketCapUsd: DisplayableNumber,
    val percentChange24h: DisplayableNumber,
    @DrawableRes val iconRes: Int,
)

data class DisplayableNumber(
    val value: Double,
    val formatted: String,
)

fun Coin.toCoinUi(): CoinUi {
    return CoinUi(
        id = id,
        name = name,
        symbol = symbol,
        rank = rank,
        priceUsd = priceUsd.toDisplayableNumber(),
        marketCapUsd = marketCapUsd.toDisplayableNumber(),
        percentChange24h = percentChange24h.toDisplayableNumber(),
        iconRes = getDrawableIdForCoin(symbol),
    )
}

fun Double.toDisplayableNumber(): DisplayableNumber {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return DisplayableNumber(
        value = this,
        formatted = formatter.format(this)
    )
}
