package com.tyabo.tyabo.navigation.chef

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tyabo.tyabo.navigation.chef.screens.ChefAddMenuDestination
import com.tyabo.tyabo.navigation.chef.screens.ChefCatalogDestination
import com.tyabo.tyabo.navigation.chef.screens.chefAddMenuComposable
import com.tyabo.tyabo.navigation.chef.screens.chefCatalogComposable

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
        chefCatalogComposable(navigateToAddMenu = { navController.navigate(ChefAddMenuDestination.route) })
        chefAddMenuComposable()
    }
}