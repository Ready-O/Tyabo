package com.tyabo.tyabo.navigation.chef.screens.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tyabo.feature.chef.profile.profile.ChefProfileScreen
import com.tyabo.tyabo.navigation.chef.ChefDestination
import com.tyabo.tyabo.navigation.chef.ChefNavScreen

object ChefProfileDestination : ChefDestination {
    override val route = "chef_profile_route"
}

fun NavGraphBuilder.ChefProfileComposable(
    navController: NavController,
    navigateToEditProfile: () -> Unit
) = composable(
    route = ChefProfileDestination.route
){
    ChefNavScreen(navController = navController) { padding ->
        ChefProfileScreen(
            modifier = Modifier.padding(padding),
            navigateToEditProfile = navigateToEditProfile
        )
    }
}