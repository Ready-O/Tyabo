package com.tyabo.feature.chef.catalog.catalog.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tyabo.data.CatalogItem
import com.tyabo.designsystem.components.MenuItem

@ExperimentalMaterial3Api
@Composable
fun ChefMenuItem(
    modifier: Modifier = Modifier,
    menuItem: CatalogItem.MenuItem,
    isExpanded: Boolean,
    editMenu: () -> Unit,
    moveMenu: () -> Unit,
    deleteMenu: () -> Unit,
    hideMenu: () -> Unit,
    unhideMenu: () -> Unit,
    onClick: () -> Unit
){
    Column(
        modifier = modifier.clickable { onClick() }
    ) {
        MenuItem(menuItem = menuItem)
        if (isExpanded){
            MenuOptions(
                editMenu = editMenu,
                moveMenu = moveMenu,
                isHidden = menuItem.isHidden,
                hideMenu = hideMenu,
                unhideMenu = unhideMenu,
                deleteMenu = deleteMenu
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun MenuOptions(
    editMenu: () -> Unit,
    moveMenu: () -> Unit,
    isHidden: Boolean,
    hideMenu: () -> Unit,
    unhideMenu: () -> Unit,
    deleteMenu: () -> Unit
) {
    Row(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        AssistChip(
            onClick = editMenu,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null
                )
              },
            label = { Text("Edit") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        if (isHidden) {
            AssistChip(
                onClick = unhideMenu,
                label = { Text("Unhide") }
            )
        } else {
            AssistChip(
                onClick = hideMenu,
                label = { Text("Hide") }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        AssistChip(
            onClick = moveMenu,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null
                )
            },
            label = { Text("Move") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        AssistChip(
            onClick = deleteMenu,
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