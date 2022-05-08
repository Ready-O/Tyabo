package com.tyabo.tyabo.navigation

sealed class Destination(hostRoute: String) {
    open val route = hostRoute
}

object SignInDestination : Destination("signin")