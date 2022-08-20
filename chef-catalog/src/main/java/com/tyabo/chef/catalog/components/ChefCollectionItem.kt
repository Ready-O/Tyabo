package com.tyabo.chef.catalog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tyabo.designsystem.CollectionItem

@Composable
fun ChefCollectionItem(
    modifier: Modifier = Modifier,
    collectionName: String,
    isExpanded: Boolean,
    editCollection: () -> Unit,
    deleteCollection: () -> Unit,
    onClick: () -> Unit
){
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .fillMaxSize()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            CollectionItem(collectionName = collectionName)
            if (isExpanded){
                CollectionOptions(
                    editCollection = editCollection,
                    deleteCollection = deleteCollection
                )
            }
        }
    }
}

@Composable
private fun CollectionOptions(
    editCollection: () -> Unit,
    deleteCollection: () -> Unit,
){
    Row {
        Button(onClick = editCollection) {
            Text(text = "Edit")
        }
        Button(onClick = deleteCollection) {
            Text(text = "Delete")
        }
    }
}