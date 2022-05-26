package com.tyabo.tyabo

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppPresenter {

    private val _bannerViewState = MutableStateFlow<BannerViewState?>(null)
    val bannerViewState = _bannerViewState.asStateFlow()

    fun displayAuthSuccess(){
        _bannerViewState.tryEmit(
            BannerViewState.Default(
                message = "Auth success"
            )
        )
    }

    fun displayAuthError(){
        _bannerViewState.tryEmit(
            BannerViewState.Error(
                message = "Auth Error"
            )
        )
    }
}