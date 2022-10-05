package com.tyabo.tyabo.navigation.chef.screens

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tyabo.feature.chef.catalog.ChefCatalogScreen
import com.tyabo.tyabo.navigation.chef.ChefDestination
import com.tyabo.tyabo.navigation.chef.ChefNavScreen

object ChefCatalogDestination : ChefDestination {
    override val route = "chef_catalog_route"
}

fun NavGraphBuilder.ChefCatalogComposable(
    navigateToEditMenu: (String?,Int?) -> Unit,
    navigateToEditCollection: (String?,String?,Boolean) -> Unit,
    navigateToReorderCatalog: (String) -> Unit,
    navController: NavController,
) = composable(
    route = ChefCatalogDestination.route
){
    ChefNavScreen(
        navController = navController
    ) {
        ChefCatalogScreen(
            navigateToEditMenu = navigateToEditMenu,
            navigateToEditCollection = navigateToEditCollection,
            navigateToReorderCatalog = navigateToReorderCatalog
        )
    }
}