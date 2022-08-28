package com.reda.chef.reordercatalog.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tyabo.data.CatalogItem
import com.tyabo.designsystem.components.CollectionItem

@Composable
fun ReorderCollections(
    collections: List<CatalogItem.CollectionItem>,
    moveUp: (Int) -> Unit,
    moveDown: (Int) -> Unit,
    confirmNewOrder: () -> Unit
){
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Button(onClick = confirmNewOrder) {
            Text(text = "Confirm")
        }
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