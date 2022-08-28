package com.tyabo.tyabo.navigation.chef

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tyabo.tyabo.navigation.chef.screens.*

@Composable
fun ChefNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ChefCatalogDestination.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        ChefCatalogComposable(
            navigateToEditMenu = {
                navController.navigate("${ChefEditMenuDestination.route}?menuId=$it")
            },
            navigateToReorderCatalog = {
                navController.navigate("${ChefReorderDestination.route}/$it")
            }
        )
        ChefEditMenuComposable(navigateUp = navController::navigateUp)
        ChefReorderComposable(navigateUp = navController::navigateUp)
    }
}