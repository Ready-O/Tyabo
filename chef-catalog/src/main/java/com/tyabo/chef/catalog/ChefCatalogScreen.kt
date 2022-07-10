package com.tyabo.chef.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyabo.data.CatalogItem
import com.tyabo.chef.catalog.components.Catalog

@Composable
fun ChefCatalogScreen(
    modifier: Modifier = Modifier,
    viewModel: ChefCatalogViewModel = hiltViewModel(),
    navigateToEditMenu: (String?) -> Unit
) {

    LaunchedEffect(Unit){
        viewModel.fetchOrderFirstTime()
    }
    val state by viewModel.catalogState.collectAsState()
    val displayState by viewModel.catalogDisplayState.collectAsState()

    when (state){
        is ChefCatalogViewState.DisplayCollection -> {
            displayCatalog(displayState = displayState, navigateToEditMenu = navigateToEditMenu)
        }
        is ChefCatalogViewState.EditCollection -> {
            Column() {
                val collectionState = state as ChefCatalogViewState.EditCollection
                TextField(value = collectionState.collection, onValueChange = viewModel::onCollectionUpdate)
                Button(onClick = { viewModel.onCollectionCtaClick(collectionState.collection) }) {
                    Text(text = "Validate Collection")
                }
                displayCatalog(displayState = displayState, navigateToEditMenu = navigateToEditMenu)
            }
        }
    }
}

@Composable
private fun displayCatalog(
    displayState: ChefCatalogDisplayViewState,
    navigateToEditMenu: (String?) -> Unit
) {
    when (displayState) {
        is ChefCatalogDisplayViewState.Loading -> {
            Button(onClick = { navigateToEditMenu(null) }) {
                Text(text = "Add Menu")
            }
        }
        is ChefCatalogDisplayViewState.Catalog -> {
            val listToDisplay = mutableListOf<CatalogItem>()
            displayState.order.forEach { catalogOrder ->
                val item = displayState.catalog.find { it.id == catalogOrder.id }
                if (item != null) {
                    listToDisplay.add(item)
                }
            }
            Column() {
                Button(onClick = { navigateToEditMenu(null) }) {
                    Text(text = "Add Menu")
                }
                Catalog(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    itemsList = listToDisplay,
                    editMenu = { navigateToEditMenu(it) }
                )
            }
        }
    }
}