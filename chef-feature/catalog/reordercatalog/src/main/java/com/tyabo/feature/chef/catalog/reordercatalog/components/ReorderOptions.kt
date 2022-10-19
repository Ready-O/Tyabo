package com.tyabo.feature.chef.catalog.reordercatalog.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun ReorderOptions(
    index: Int,
    lastIndex: Int,
    moveUp: (Int) -> Unit,
    moveDown: (Int) -> Unit
) {
    Row() {
        if (index != 0) {
            IconButton(onClick = { moveUp(index) }) {
                Icon(imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = null)
            }
        }
        if (index != lastIndex) {
            IconButton(onClick = { moveDown(index) }) {
                Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = null)
            }
        }
    }
    if (index != lastIndex) {
        Divider(
            color = MaterialTheme.colorScheme.surfaceVariant,
            thickness = 1.dp
        )
    }
}