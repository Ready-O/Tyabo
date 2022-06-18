package com.tyabo.tyabo.navigation.chef.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tyabo.tyabo.navigation.chef.ChefDestination

object ChefCatalogDestination : ChefDestination {
    override val route = "chef_catalog_route"
}

fun NavGraphBuilder.chefCatalogComposable() = composable(
    route = ChefCatalogDestination.route
){
    LazyColumn(){

    }
    Button(onClick = {}) {
        Text(text = "Add Menu")
    }
}