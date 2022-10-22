package com.tyabo.tyabo.navigation.chef.screens.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tyabo.feature.chef.profile.editprofile.ChefEditProfileScreen
import com.tyabo.tyabo.navigation.chef.ChefDestination

object ChefEditProfileDestination : ChefDestination {
    override val route = "chef_edit_profile_route"
}

fun NavGraphBuilder.ChefEditProfileComposable(
    navigateUp: () -> Unit
) = composable(
    route = ChefEditProfileDestination.route
){
    ChefEditProfileScreen(navigateUp = navigateUp)
}