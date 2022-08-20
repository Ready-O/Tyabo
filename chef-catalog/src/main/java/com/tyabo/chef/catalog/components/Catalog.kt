package com.tyabo.chef.catalog.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.tyabo.data.CatalogItem
import com.tyabo.designsystem.CollectionItem

@Composable
fun Catalog(
    modifier: Modifier = Modifier,
    itemsList: List<CatalogItem>,
    addMenu: () -> Unit = {},
    editMenu: (String) -> Unit = {},
    hideMenu: (String) -> Unit = {},
    unhideMenu: (String) -> Unit = {},
    deleteMenu: (String) -> Unit = {},
    editCollection: (CatalogItem.CollectionItem) -> Unit = {}
    ){
        val itemToExpand: MutableState<String?> = remember { mutableStateOf(null) }
        val collectionToEdit: MutableState<String?> = remember { mutableStateOf(null) }

        LazyColumn(
            modifier = modifier
        ) {
            items(itemsList) { item ->
                when(item){
                    is CatalogItem.MenuItem -> {
                        ChefMenuItem(
                            modifier = Modifier.padding(horizontal = 16.dp,vertical = 12.dp),
                            menuItem = item,
                            isExpanded = itemToExpand.value == item.id,
                            editMenu = { editMenu(item.id) },
                            deleteMenu = { deleteMenu(item.id) },
                            hideMenu = { hideMenu(item.id) },
                            unhideMenu = { unhideMenu(item.id) },
                            onClick = {
                                switchExpand(itemToExpand, item)
                            }
                        )
                    }
                    is CatalogItem.CollectionItem -> {
                        val collectionName = remember { mutableStateOf(item.name) }
                        when(collectionToEdit.value){
                            null -> ChefCollectionItem(
                                collectionName = collectionName.value,
                                isExpanded = itemToExpand.value == item.id,
                                editCollection = { collectionToEdit.value = item.id },
                                deleteCollection = {},
                                onClick = { switchExpand(itemToExpand, item) }
                            )
                            item.id -> EditCollectionItem(
                                collectionName = collectionName.value,
                                cancel = {
                                    collectionToEdit.value = null
                                },
                                editCollection = {
                                    collectionName.value = it
                                    collectionToEdit.value = null
                                    itemToExpand.value = null
                                    editCollection(item.copy(name = it))
                                }
                            )
                            else -> ChefCollectionItem(
                                collectionName = collectionName.value,
                                isExpanded = false,
                                editCollection = {},
                                deleteCollection = {},
                                onClick = {}
                            )
                        }
                    }
                }
            }
        }
}

private fun switchExpand(
    itemToExpand: MutableState<String?>,
    item: CatalogItem
) {
    if (itemToExpand.value == item.id) {
        itemToExpand.value = null
    } else {
        itemToExpand.value = item.id
    }
}