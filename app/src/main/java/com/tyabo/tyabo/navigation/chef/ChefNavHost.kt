package com.tyabo.tyabo.navigation.chef

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tyabo.tyabo.navigation.chef.screens.*
import com.tyabo.tyabo.navigation.chef.screens.catalog.ChefCatalogGraph
import com.tyabo.tyabo.navigation.chef.screens.catalog.ChefCatalogGraphDestination
import com.tyabo.tyabo.navigation.chef.screens.profile.ChefProfileGraph

@Composable
fun ChefNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ChefCatalogGraphDestination.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        ChefCatalogGraph(navController)
        ChefProfileGraph(navController)
    }
}