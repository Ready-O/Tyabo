package com.tyabo.feature.chef.catalog.catalog.components

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
import com.tyabo.designsystem.components.buttons.ElevatedButton
import com.tyabo.designsystem.components.buttons.TextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChefCatalog(
    modifier: Modifier = Modifier,
    itemsList: List<CatalogItem>,
    addMenu: (Int) -> Unit = {},
    editMenu: (String) -> Unit = {},
    moveMenu: (String) -> Unit = {},
    hideMenu: (CatalogItem.MenuItem) -> Unit = {},
    unhideMenu: (CatalogItem.MenuItem) -> Unit = {},
    deleteMenu: (String) -> Unit = {},
    addCollection: () -> Unit = {},
    editCollection: (CatalogItem.CollectionItem) -> Unit = {},
    moveCollection: (String) -> Unit = {},
    deleteCollection: (String) -> Unit = {},
){
    if (itemsList.isNotEmpty()){
        val itemToExpand: MutableState<String?> = remember { mutableStateOf(null) }

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
                        AddMenuOrCollection(
                            index = index,
                            itemsList = itemsList,
                            addMenuClick = { addMenu(index+1) },
                            addCollectionClick = addCollection
                        )
                    }
                    is CatalogItem.CollectionItem -> {
                        ChefCollectionItem(
                            collectionName = item.name,
                            isExpanded = itemToExpand.value == item.id,
                            editCollection = { editCollection(item) },
                            moveCollection = { moveCollection(item.id) },
                            deleteCollection = { deleteCollection(item.id) },
                            onClick = { switchExpand(itemToExpand, item) }
                        )
                        AddMenuOrCollection(
                            index = index,
                            itemsList = itemsList,
                            addMenuClick = { addMenu(index+1) },
                            addCollectionClick = addCollection
                        )
                    }
                }
            }
        }
    }
    else{
        AddCollectionButton(addCollection)
    }
}

@Composable
private fun AddMenuOrCollection(
    index: Int,
    itemsList: List<CatalogItem>,
    addMenuClick: () -> Unit,
    addCollectionClick: () -> Unit,
) {
    if (index == itemsList.size - 1 || itemsList[index + 1] is CatalogItem.CollectionItem) {
        Column() {
            AddMenuButton(addMenuClick)
            if(index == itemsList.size - 1 ){
                AddCollectionButton(addCollectionClick)
            }
        }
    }
}

@Composable
private fun AddCollectionButton(addCollectionClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        ElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Filled.Add,
            onClick = addCollectionClick
        ) {
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = "Add Collection",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun AddMenuButton(addMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Filled.Add,
            onClick = addMenuClick
        ) {
            Text(
                text = "Add Menu",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
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