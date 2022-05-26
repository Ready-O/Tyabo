package com.tyabo.tyabo

sealed class BannerViewState{
    class Default(val message: String): BannerViewState()
    class Error(val message: String): BannerViewState()
}
