package com.tyabo.chef.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyabo.data.CatalogItem
import com.tyabo.data.Collection
import com.tyabo.data.Menu
import timber.log.Timber

@Composable
fun ChefCatalogScreen(
    modifier: Modifier = Modifier,
    viewModel: ChefCatalogViewModel = hiltViewModel(),
    navigateToEditMenu: () -> Unit
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
    navigateToEditMenu: () -> Unit
) {
    when (displayState) {
        is ChefCatalogDisplayViewState.Loading -> {
            Button(onClick = navigateToEditMenu) {
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
                Button(onClick = navigateToEditMenu) {
                    Text(text = "Add Menu")
                }
                LazyColumn() {
                    items(listToDisplay) { item ->
                        Row() {
                            when(item){
                                is CatalogItem.MenuItem -> {
                                    Text(text = item.name)
                                    Text(text = item.price.toString())
                                }
                                is CatalogItem.CollectionItem -> {
                                    Text(text = item.name)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}