package com.tyabo.tyabo.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppPresenter {

    private val _bannerViewState = MutableStateFlow<BannerViewState?>(null)
    val bannerViewState = _bannerViewState.asStateFlow()

    fun displayAuthSuccess(name: String){
        _bannerViewState.tryEmit(
            BannerViewState.Default(
                message = "Auth success $name"
            )
        )
    }

    fun displayAuthError(){
        _bannerViewState.tryEmit(
            BannerViewState.Error(
                message = "Authentication failed"
            )
        )
    }
}