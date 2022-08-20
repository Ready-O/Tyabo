package com.tyabo.chef.catalog.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.tyabo.data.CatalogItem
import com.tyabo.designsystem.MenuItem

@Composable
fun ChefMenuItem(
    modifier: Modifier = Modifier,
    menuItem: CatalogItem.MenuItem,
    isExpanded: Boolean,
    editMenu: () -> Unit,
    deleteMenu: () -> Unit,
    hideMenu: () -> Unit,
    unhideMenu: () -> Unit,
    onClick: () -> Unit
){
    val isHidden = remember { mutableStateOf(menuItem.isHidden) }

    Column(
        modifier = modifier
    ) {
        MenuItem(
            menuItem = menuItem,
            onClick = onClick
        )
        if (isExpanded){
            MenuOptions(
                editMenu = editMenu,
                isHidden = isHidden,
                hideMenu = hideMenu,
                unhideMenu = unhideMenu,
                deleteMenu = deleteMenu
            )
        }
    }
}

@Composable
private fun MenuOptions(
    editMenu: () -> Unit,
    isHidden: MutableState<Boolean>,
    hideMenu: () -> Unit,
    unhideMenu: () -> Unit,
    deleteMenu: () -> Unit
) {
    Row() {
        Button(onClick = editMenu) {
            Text(text = "Edit")
        }
        if (isHidden.value) {
            Button(onClick = {
                isHidden.value = false
                unhideMenu()
            }) {
                Text("Unhide")
            }
        } else {
            Button(onClick = {
                isHidden.value = true
                hideMenu()
            }) {
                Text("Hide")
            }
        }
        Button(onClick = deleteMenu) {
            Text(text = "Delete")
        }
    }
}