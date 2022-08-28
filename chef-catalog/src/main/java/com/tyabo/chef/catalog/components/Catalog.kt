package com.tyabo.chef.catalog.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tyabo.data.CatalogItem
import com.tyabo.designsystem.components.TextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Catalog(
    modifier: Modifier = Modifier,
    itemsList: List<CatalogItem>,
    addMenu: () -> Unit = {},
    editMenu: (String) -> Unit = {},
    moveMenu: (String) -> Unit = {},
    hideMenu: (CatalogItem.MenuItem) -> Unit = {},
    unhideMenu: (CatalogItem.MenuItem) -> Unit = {},
    deleteMenu: (String) -> Unit = {},
    editCollection: (CatalogItem.CollectionItem) -> Unit = {},
    moveCollection: (String) -> Unit = {}
    ){
        val itemToExpand: MutableState<String?> = remember { mutableStateOf(null) }
        val collectionToEdit: MutableState<String?> = remember { mutableStateOf(null) }

        LazyColumn(
            modifier = modifier
        ) {
            itemsIndexed(itemsList) { index, item ->
                when(item){
                    is CatalogItem.MenuItem -> {
                        ChefMenuItem(
                            modifier = Modifier.padding(vertical = 12.dp),
                            menuItem = item,
                            isExpanded = itemToExpand.value == item.id,
                            editMenu = { editMenu(item.id) },
                            moveMenu = { moveMenu(item.id) },
                            deleteMenu = { deleteMenu(item.id) },
                            hideMenu = { hideMenu(item) },
                            unhideMenu = { unhideMenu(item) },
                            onClick = {
                                switchExpand(itemToExpand, item)
                            }
                        )
                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                        AddMenu(index, itemsList)
                    }
                    is CatalogItem.CollectionItem -> {
                        when(collectionToEdit.value){
                            null -> ChefCollectionItem(
                                collectionName = item.name,
                                isExpanded = itemToExpand.value == item.id,
                                editCollection = { collectionToEdit.value = item.id },
                                moveCollection = { moveCollection(item.id) },
                                deleteCollection = {},
                                onClick = { switchExpand(itemToExpand, item) }
                            )
                            item.id -> EditCollectionItem(
                                collectionName = item.name,
                                cancel = {
                                    collectionToEdit.value = null
                                },
                                editCollection = {
                                    collectionToEdit.value = null
                                    itemToExpand.value = null
                                    editCollection(item.copy(name = it))
                                }
                            )
                            else -> ChefCollectionItem(
                                collectionName = item.name,
                                isExpanded = false,
                                editCollection = {},
                                moveCollection = { moveCollection(item.id) },
                                deleteCollection = {},
                                onClick = {}
                            )
                        }
                        AddMenu(index, itemsList)
                    }
                }
            }
        }
}

@Composable
private fun AddMenu(
    index: Int,
    itemsList: List<CatalogItem>
) {
    if (index == itemsList.size - 1 || itemsList[index + 1] is CatalogItem.CollectionItem) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                leadingIcon = Icons.Filled.Add,
                onClick = {}
            ) {
                Text(
                    text = "Add Menu",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
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