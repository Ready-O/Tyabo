package com.tyabo.chef.catalog

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChefCatalogScreen(
    modifier: Modifier = Modifier,
    viewModel: ChefCatalogViewModel = hiltViewModel(),
    navigateToEditMenu: () -> Unit
) {
    Button(onClick = navigateToEditMenu) {
        Text(text = "Add Menu")
    }
}