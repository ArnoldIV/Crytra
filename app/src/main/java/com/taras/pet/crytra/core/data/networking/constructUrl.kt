package com.taras.pet.crytra.core.data.networking

import com.taras.pet.crytra.BuildConfig

fun constructUrl(url: String): String{
    return when{
        url.contains(BuildConfig.BASE_URL) -> url
        url.startsWith("/") -> BuildConfig.BASE_URL + url.drop(1) // drop a first /
        else -> BuildConfig.BASE_URL + url
    }
}