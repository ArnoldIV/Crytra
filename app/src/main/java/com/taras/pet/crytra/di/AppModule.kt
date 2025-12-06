package com.taras.pet.crytra.di

import com.taras.pet.crytra.core.data.networking.HttpClientFactory
import com.taras.pet.crytra.crypto.data.networking.RemoteCoinDataSource
import com.taras.pet.crytra.crypto.domain.CoinDataSource
import com.taras.pet.crytra.crypto.presentation.coin_list.CoinListViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create() ) }
    singleOf(::RemoteCoinDataSource).bind<CoinDataSource>()

    viewModelOf(::CoinListViewModel)
}