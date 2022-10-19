package com.tyabo.feature.chef.catalog.reordercatalog.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tyabo.data.CatalogItem
import com.tyabo.designsystem.components.CollectionItem
import com.tyabo.designsystem.components.MenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReorderMenus(
    parentCollection: CatalogItem.CollectionItem,
    menus: List<CatalogItem.MenuItem>,
    moveUp: (Int) -> Unit,
    moveDown: (Int) -> Unit,
    navigateUp: () -> Unit,
    confirmNewOrder: () -> Unit
){
    Scaffold(
        topBar = {
            TopBar(
                navigateUp = navigateUp,
                onCtaClicked = confirmNewOrder
            )
        }
    ) { paddingForBars ->
        Column(modifier = Modifier.padding(paddingForBars).padding(horizontal = 16.dp)) {
            ParentCollection(parentCollection)
            LazyColumn{
                itemsIndexed(menus){index, item ->
                    Spacer(modifier = Modifier.size(8.dp))
                    MenuItem(menuItem = item)
                    ReorderOptions(
                        index = index,
                        lastIndex = menus.size - 1,
                        moveUp = moveUp,
                        moveDown = moveDown
                    )
                }
            }
        }
    }
}

@Composable
private fun ParentCollection(parentCollection: CatalogItem.CollectionItem) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        CollectionItem(collectionName = parentCollection.name)
        Divider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 2.dp,
            MaterialTheme.colorScheme.onBackground
        )
    }
}