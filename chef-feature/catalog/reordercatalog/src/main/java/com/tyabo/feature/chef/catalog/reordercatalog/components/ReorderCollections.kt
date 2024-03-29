package com.tyabo.feature.chef.catalog.reordercatalog.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tyabo.data.CatalogItem
import com.tyabo.designsystem.components.CollectionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReorderCollections(
    collections: List<CatalogItem.CollectionItem>,
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
            LazyColumn {
                itemsIndexed(collections) { index, item ->
                    Spacer(modifier = Modifier.size(8.dp))
                    CollectionItem(collectionName = item.name)
                    ReorderOptions(
                        index = index,
                        lastIndex = collections.size - 1,
                        moveUp = moveUp,
                        moveDown = moveDown
                    )
                }
            }
        }
    }
}