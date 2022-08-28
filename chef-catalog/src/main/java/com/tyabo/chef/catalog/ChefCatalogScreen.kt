package com.tyabo.chef.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
    viewModel: ChefCatalogViewModel = hiltViewModel(),
    navigateToEditMenu: (String?) -> Unit,
    navigateToReorderCatalog: (String) -> Unit
) {

    LaunchedEffect(Unit){
        viewModel.fetchOrderFirstTime()
    }

    val state by viewModel.catalogState.collectAsState()
    viewModel.fetchedCatalog.collectAsState()
    val displayState by viewModel.catalogDisplayState.collectAsState()

    when (state){
        is ChefCatalogViewState.DisplayCatalog -> {
            displayCatalog(
                catalogState = displayState,
                navigateToEditMenu = navigateToEditMenu,
                reorderCatalog = navigateToReorderCatalog,
                hideMenu = viewModel::hideMenu,
                unhideMenu = viewModel::unhideMenu,
                deleteMenu = viewModel::deleteMenu,
                editCollection = viewModel::editCollection,
                moveCollection = navigateToReorderCatalog,
                deleteCollection = viewModel::deleteCollection
            )
        }
        is ChefCatalogViewState.AddCollection -> {
            Column() {
                val collectionState = state as ChefCatalogViewState.AddCollection
                TextField(value = collectionState.collection, onValueChange = viewModel::onCollectionUpdate)
                Button(onClick = { viewModel.addNewCollection(collectionState.collection) }) {
                    Text(text = "Validate Collection")
                }
                displayCatalog(
                    catalogState = displayState,
                    navigateToEditMenu = navigateToEditMenu,
                    reorderCatalog = navigateToReorderCatalog,
                    hideMenu = viewModel::hideMenu,
                    unhideMenu = viewModel::unhideMenu,
                    deleteMenu = viewModel::deleteMenu,
                    editCollection = viewModel::editCollection,
                    moveCollection = navigateToReorderCatalog,
                    deleteCollection = viewModel::deleteCollection
                )
            }
        }
    }
}

@Composable
private fun displayCatalog(
    catalogState: ChefCatalogDisplayViewState,
    navigateToEditMenu: (String?) -> Unit,
    reorderCatalog: (String) -> Unit,
    hideMenu: (CatalogItem.MenuItem) -> Unit,
    unhideMenu: (CatalogItem.MenuItem) -> Unit,
    deleteMenu: (String) -> Unit,
    editCollection: (CatalogItem.CollectionItem) -> Unit,
    moveCollection: (String) -> Unit,
    deleteCollection: (String) -> Unit
) {
    when (catalogState) {
        is ChefCatalogDisplayViewState.Loading -> {
            Button(onClick = { navigateToEditMenu(null) }) {
                Text(text = "Add Menu")
            }
        }
        is ChefCatalogDisplayViewState.Catalog -> {
            Column() {
                Button(onClick = { navigateToEditMenu(null) }) {
                    Text(text = "Add Menu")
                }
                Catalog(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    itemsList = catalogState.catalog,
                    editMenu = { navigateToEditMenu(it) },
                    moveMenu = reorderCatalog,
                    hideMenu = hideMenu,
                    unhideMenu = unhideMenu,
                    deleteMenu = deleteMenu,
                    editCollection = editCollection,
                    moveCollection = moveCollection,
                    deleteCollection = deleteCollection
                )
            }

        }
    }

}