package com.tyabo.tyabo.navigation.chef.screens.catalog

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.tyabo.tyabo.navigation.chef.ChefDestination

object ChefCatalogGraphDestination : ChefDestination {
    override val route = "chef_catalog_graph_route"
}

fun NavGraphBuilder.ChefCatalogGraph(
    navController: NavController
){
    navigation(
        route = ChefCatalogGraphDestination.route,
        startDestination = ChefCatalogDestination.route
    ){
        ChefCatalogComposable(
            navigateToEditMenu = { menuId, posIndex ->
                navController.navigate("${ChefEditMenuDestination.route}?${if (menuId != null) "menuId=$menuId&posIndex=${posIndex?: 0}" else "posIndex=${posIndex?: 0}"}")
            },
            navigateToEditCollection = { collectionId, collectionName, isNew ->
                navController.navigate(
                    "${ChefEditCollectionDestination.route}?collectionId=$collectionId&collectionName=${collectionName?: ""}&isNew=$isNew"
                )
            },
            navigateToReorderCatalog = { navController.navigate("${ChefReorderDestination.route}/$it") },
            navController = navController
        )
        ChefEditMenuComposable(navigateUp = navController::navigateUp)
        ChefEditCollectionComposable(navigateUp = navController::navigateUp)
        ChefReorderComposable(navigateUp = navController::navigateUp)
    }
}