package com.tyabo.chef.catalog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tyabo.designsystem.CollectionItem

@ExperimentalMaterial3Api
@Composable
fun ChefCollectionItem(
    modifier: Modifier = Modifier,
    collectionName: String,
    isExpanded: Boolean,
    editCollection: () -> Unit,
    deleteCollection: () -> Unit,
    onClick: () -> Unit
){
    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxSize()
            .clickable { onClick() }
    ) {
        CollectionItem(collectionName = collectionName)
        if (isExpanded){
            CollectionOptions(
                editCollection = editCollection,
                deleteCollection = deleteCollection
            )
        }
        Divider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 3.dp
        )
    }
}

@ExperimentalMaterial3Api
@Composable
private fun CollectionOptions(
    editCollection: () -> Unit,
    deleteCollection: () -> Unit,
){
    Row {
        AssistChip(
            onClick = editCollection,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null
                )
            },
            label = { Text("Edit") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        AssistChip(
            onClick = deleteCollection,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null
                )
            },
            label = { Text("Delete") }
        )
    }
}