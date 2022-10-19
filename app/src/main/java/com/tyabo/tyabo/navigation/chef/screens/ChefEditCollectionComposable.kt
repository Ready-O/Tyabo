package com.tyabo.tyabo.navigation.chef.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tyabo.feature.chef.catalog.editcollection.ChefEditCollectionScreen
import com.tyabo.tyabo.navigation.chef.ChefDestination

object ChefEditCollectionDestination : ChefDestination {
    override val route = "chef_edit_collection_route"
}

fun NavGraphBuilder.ChefEditCollectionComposable(navigateUp: () -> Unit) = composable(
    route = "${ChefEditCollectionDestination.route}?collectionId={collectionId}&collectionName={collectionName}&isNew={isNew}",
    arguments = listOf(
        navArgument("collectionId") {
            nullable = true
            type = NavType.StringType
        },
        navArgument("collectionName") {
            nullable = false
            type = NavType.StringType
        },
        navArgument("isNew") {
            nullable = false
            type = NavType.BoolType
        }
    )
){
    ChefEditCollectionScreen(
        navigateUp = navigateUp
    )
}
