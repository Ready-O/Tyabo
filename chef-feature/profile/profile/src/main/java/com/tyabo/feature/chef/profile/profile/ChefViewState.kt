package com.tyabo.feature.chef.profile.profile

import com.tyabo.data.Chef

sealed interface ChefViewState {

    object Loading : ChefViewState

    data class ChefMainInfo(
        val chef: Chef
    ): ChefViewState
}