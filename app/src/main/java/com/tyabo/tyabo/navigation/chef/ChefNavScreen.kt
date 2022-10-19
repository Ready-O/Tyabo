package com.tyabo.tyabo.navigation.chef

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.tyabo.designsystem.components.NavigationBar
import com.tyabo.designsystem.components.MainDestination
import com.tyabo.tyabo.navigation.chef.screens.catalog.ChefCatalogDestination
import com.tyabo.tyabo.navigation.chef.screens.profile.ChefProfileDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChefNavScreen(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
){
    Scaffold(
        bottomBar = {
            val selectedRoute = navController.currentBackStackEntry?.destination?.route
            NavigationBar(
                selectedRoute = selectedRoute,
                destinations = listDestinations,
                navigate = { newRoute ->
                    if (selectedRoute != newRoute){
                        navController.navigate(newRoute){
                            popUpTo(selectedRoute!!){
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }
    ) { paddingForBars ->
        content(paddingForBars)
    }
}

private val listDestinations = listOf(
    MainDestination(
        route = ChefCatalogDestination.route,
        selectedIcon = Icons.Filled.List,
        unselectedIcon = Icons.Outlined.List,
    ),
    MainDestination(
        route = ChefProfileDestination.route,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
)