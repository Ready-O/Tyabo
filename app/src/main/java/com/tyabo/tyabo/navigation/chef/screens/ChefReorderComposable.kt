package com.tyabo.tyabo.navigation.chef.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.reda.chef.reordercatalog.ChefReorderScreen
import com.tyabo.tyabo.navigation.chef.ChefDestination

object ChefReorderDestination : ChefDestination {
    override val route = "chef_reorder_catalog_route"
}

fun NavGraphBuilder.ChefReorderComposable(navigateUp: () -> Unit) = composable(
    route = "${ChefReorderDestination.route}/{catalogItemId}",
    arguments =  listOf(navArgument("catalogItemId") {
        nullable = false
        type = NavType.StringType
    }
    )
){
    ChefReorderScreen(navigateUp = navigateUp)
}