package com.tyabo.tyabo

import android.app.Application
import com.tyabo.tyabo.di.allKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

open class TyaboApp : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        // Setup dependency injection
        startKoin {
            androidContext(applicationContext)
            modules(allKoinModules)
        }
    }
}