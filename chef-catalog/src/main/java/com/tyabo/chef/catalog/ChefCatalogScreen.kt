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
    navigateToAddMenu: () -> Unit
) {
    Button(onClick = navigateToAddMenu) {
        Text(text = "Add Menu")
    }
}