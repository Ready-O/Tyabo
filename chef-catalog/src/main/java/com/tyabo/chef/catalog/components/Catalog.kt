package com.tyabo.chef.catalog.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyabo.data.CatalogItem
import com.tyabo.designsystem.MenuItem

@Composable
fun Catalog(
    modifier: Modifier,
    itemsList: List<CatalogItem>,
    addMenu: () -> Unit = {},
    clickMenu: (String) -> Unit = {},
    editCollection: (CatalogItem.CollectionItem) -> Unit = {}
    ){
    LazyColumn(
        modifier = modifier
    ) {
        items(itemsList) { item ->
            Row() {
                when(item){
                    is CatalogItem.MenuItem -> {
                        MenuItem(
                            modifier = Modifier.padding(vertical = 12.dp),
                            menuItem = item,
                            editMenu = clickMenu
                        )
                    }
                    is CatalogItem.CollectionItem -> {
                        val editableState = remember { mutableStateOf(false) }
                        val collectionName = remember { mutableStateOf(item.name) }
                        CollectionItem(
                            collectionName = collectionName.value,
                            editable = editableState.value,
                            switchState = { state -> editableState.value = state },
                        ) {
                            collectionName.value = it
                            editCollection(
                                CatalogItem.CollectionItem(id = item.id, name = it)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CollectionItem(
    modifier: Modifier = Modifier,
    collectionName: String,
    editable: Boolean,
    switchState: (Boolean) -> Unit,
    editCollection: (String) -> Unit,
){
    val keyboardController = LocalSoftwareKeyboardController.current

    if (editable){
        val name = remember { mutableStateOf(collectionName) }
        Column() {
            TextField(value = name.value, onValueChange = { name.value = it })
            Row{
                Button(onClick = { switchState(false)}) {
                    Text("Annuler")
                }
                Button(onClick = {
                    keyboardController?.hide()
                    editCollection(name.value)
                    switchState(false)
                }
                ) {
                    Text("Valider")
                }
            }
        }
    }
    else{
        Text(
            modifier = modifier.clickable { switchState(true) },
            text = collectionName,
            color = Color.Black,
            fontSize = 28.sp
        )
    }
}