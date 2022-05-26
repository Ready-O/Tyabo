package com.tyabo.tyabo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TyaboApp : Application() {
    override fun onCreate() {
        super.onCreate()
        configureTimber()
    }

    private fun configureTimber(){
        Timber.plant(Timber.DebugTree());
    }
}