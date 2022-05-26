package com.tyabo.tyabo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TyaboApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}