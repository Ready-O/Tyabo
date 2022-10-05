package com.tyabo.tyabo.navigation.chef.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tyabo.feature.chef.editmenu.ChefEditMenuScreen
import com.tyabo.tyabo.navigation.chef.ChefDestination

object ChefEditMenuDestination : ChefDestination {
    override val route = "chef_edit_menu_route"
}

fun NavGraphBuilder.ChefEditMenuComposable(navigateUp: () -> Unit) = composable(
    route = "${ChefEditMenuDestination.route}?menuId={menuId}&posIndex={posIndex}",
    arguments = listOf(
        navArgument("menuId") {
            nullable = true
            type = NavType.StringType
        },
        navArgument("posIndex") {
            nullable = false
            type = NavType.IntType
        }
    )
){
    ChefEditMenuScreen(navigateUp = navigateUp)
}