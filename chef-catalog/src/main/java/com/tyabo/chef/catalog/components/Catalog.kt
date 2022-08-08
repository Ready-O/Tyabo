package com.tyabo.chef.catalog.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyabo.data.CatalogItem
import com.tyabo.designsystem.MenuItem

@Composable
fun Catalog(
    modifier: Modifier,
    itemsList: List<CatalogItem>,
    addMenu: () -> Unit = {},
    editMenu: (String) -> Unit = {},
    editCollection: () -> Unit = {}
    ){
    LazyColumn(
        modifier = modifier
    ) {
        items(itemsList) { item ->
            Row() {
                when(item){
                    is CatalogItem.MenuItem -> {
                        MenuItem(
                            modifier = Modifier.padding(vertical = 12.dp),
                            menuItem = item,
                            editMenu = editMenu
                        )
                    }
                    is CatalogItem.CollectionItem -> {
                        collectionItem(collectionItem = item)
                    }
                }
            }
        }
    }
}

@Composable
fun collectionItem(
    modifier: Modifier = Modifier,
    collectionItem: CatalogItem.CollectionItem
){
    Text(
        modifier = modifier,
        text = collectionItem.name,
        color = Color.Black,
        fontSize = 28.sp
    )
}