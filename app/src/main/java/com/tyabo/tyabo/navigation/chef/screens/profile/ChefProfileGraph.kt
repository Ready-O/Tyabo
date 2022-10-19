package com.tyabo.tyabo.navigation.chef.screens.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.tyabo.tyabo.navigation.chef.ChefDestination

object ChefProfileGraphDestination : ChefDestination {
    override val route = "chef_profile_graph_route"
}

fun NavGraphBuilder.ChefProfileGraph(
    navController: NavController
){
    navigation(
        route = ChefProfileGraphDestination.route,
        startDestination = ChefProfileDestination.route
    ){
        ChefProfileComposable(navController = navController)
    }
}