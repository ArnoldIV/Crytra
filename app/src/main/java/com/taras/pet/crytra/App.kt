package com.taras.pet.crytra

import android.app.Application
import com.taras.pet.crytra.di.appModule
import io.ktor.http.ContentType
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger()

            modules(appModule)
        }
    }
}