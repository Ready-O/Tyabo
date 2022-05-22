package com.tyabo.tyabo.di

import com.tyabo.tyabo.features.AuthViewModel
import com.tyabo.tyabo.navigation.SignInDestination
import com.tyabo.tyabo.navigation.scopedNav.Clearable
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    scope<SignInDestination>{
        scoped {
            AuthViewModel()
        }
    }
}

val allKoinModules = appModule