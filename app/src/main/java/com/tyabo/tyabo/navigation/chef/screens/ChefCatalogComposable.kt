package com.tyabo.tyabo.navigation.chef.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tyabo.chef.catalog.ChefCatalogScreen
import com.tyabo.tyabo.navigation.chef.ChefDestination

object ChefCatalogDestination : ChefDestination {
    override val route = "chef_catalog_route"
}

fun NavGraphBuilder.ChefCatalogComposable(
    navigateToEditMenu: (String?,Int?) -> Unit,
    navigateToEditCollection: (String?,String?,Boolean) -> Unit,
    navigateToReorderCatalog: (String) -> Unit,
) = composable(
    route = ChefCatalogDestination.route
){
    ChefCatalogScreen(
        navigateToEditMenu = navigateToEditMenu,
        navigateToEditCollection = navigateToEditCollection,
        navigateToReorderCatalog = navigateToReorderCatalog
    )
}