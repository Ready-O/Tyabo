package com.tyabo.tyabo.navigation.chef.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tyabo.chef.editmenu.ChefEditMenuScreen
import com.tyabo.tyabo.navigation.chef.ChefDestination

object ChefEditMenuDestination : ChefDestination {
    override val route = "chef_add_menu_route"
}

fun NavGraphBuilder.chefEditMenuComposable(navigateUp: () -> Unit) = composable(
    route = ChefEditMenuDestination.route
){
    ChefEditMenuScreen(navigateUp = navigateUp)
}