package com.tyabo.tyabo.navigation.chef.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tyabo.tyabo.navigation.chef.ChefDestination

object ChefAddMenuDestination : ChefDestination {
    override val route = "chef_add_menu_route"
}

fun NavGraphBuilder.chefAddMenuComposable() = composable(
    route = ChefAddMenuDestination.route
){
}