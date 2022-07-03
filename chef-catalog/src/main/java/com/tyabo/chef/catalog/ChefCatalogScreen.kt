package com.tyabo.chef.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyabo.data.CatalogOrder
import com.tyabo.data.Menu

@Composable
fun ChefCatalogScreen(
    modifier: Modifier = Modifier,
    viewModel: ChefCatalogViewModel = hiltViewModel(),
    navigateToEditMenu: () -> Unit
) {

    val state by viewModel.catalogState.collectAsState()

    when(state){
        is ChefCatalogViewState.Loading -> {
            Button(onClick = navigateToEditMenu) {
                Text(text = "Add Menu")
            }
        }
        is ChefCatalogViewState.Catalog -> {
            val catalogState = state as ChefCatalogViewState.Catalog

            val listToDisplay = mutableListOf<Menu>()
            catalogState.order.forEach { catalogOrder ->
                val menu = catalogState.catalog.find { it.id == catalogOrder.id }
                if (menu != null){
                    listToDisplay.add(menu)
                }
            }

            Column() {
                Button(onClick = navigateToEditMenu) {
                    Text(text = "Add Menu")
                }
                LazyColumn(){
                    items(listToDisplay) { menu ->
                        Row() {
                            Text(text = menu.id)
                            Text(text = menu.name)
                        }
                    }
                }
            }
        }
    }
}