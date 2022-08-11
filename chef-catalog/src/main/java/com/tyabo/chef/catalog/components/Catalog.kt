package com.tyabo.chef.catalog.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
    hideMenu: (String) -> Unit = {},
    unhideMenu: (String) -> Unit = {},
    editCollection: (CatalogItem.CollectionItem) -> Unit = {}
    ){
        val collectionToEdit: MutableState<String?> = remember { mutableStateOf(null) }
        LazyColumn(
            modifier = modifier
        ) {
            items(itemsList) { item ->
                when(item){
                    is CatalogItem.MenuItem -> {
                        val isHidden = remember { mutableStateOf(item.isHidden) }
                        MenuItem(
                            modifier = Modifier.padding(vertical = 12.dp),
                            menuItem = item,
                            editMenu = clickMenu
                        )
                        if (isHidden.value){
                            Button(onClick = {
                                isHidden.value = false
                                unhideMenu(item.id)
                            }) {
                                Text("Unhide")
                            }
                        }
                        else{
                            Button(onClick = {
                                isHidden.value = true
                                hideMenu(item.id)
                            }) {
                                Text("Hide")
                            }
                        }
                    }
                    is CatalogItem.CollectionItem -> {
                        val collectionName = remember { mutableStateOf(item.name) }
                        when(collectionToEdit.value){
                            null -> CollectionItem(
                                modifier = Modifier.clickable { collectionToEdit.value = item.id },
                                collectionName = collectionName.value
                            )
                            item.id -> EditCollectionItem(
                                collectionName = collectionName.value,
                                cancel = {
                                    collectionToEdit.value = null
                                },
                                editCollection = {
                                    collectionName.value = it
                                    collectionToEdit.value = null
                                    editCollection(item.copy(name = it))
                                }
                            )
                            else -> CollectionItem(collectionName = collectionName.value)
                        }
                    }
                }
            }
        }
}

@Composable
fun CollectionItem(
    modifier: Modifier = Modifier,
    collectionName: String,
){
    Text(
        modifier = modifier,
        text = collectionName,
        color = Color.Black,
        fontSize = 28.sp
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditCollectionItem(
    modifier: Modifier = Modifier,
    collectionName: String,
    cancel: () -> Unit,
    editCollection: (String) -> Unit,
){
    val keyboardController = LocalSoftwareKeyboardController.current

    val name = remember { mutableStateOf(collectionName) }
    Column() {
        TextField(value = name.value, onValueChange = { name.value = it })
        Row{
            Button(onClick = {
                keyboardController?.hide()
                cancel()
            }) {
                Text("Annuler")
            }
            Button(onClick = {
                keyboardController?.hide()
                editCollection(name.value)
            }
            ) {
                Text("Valider")
            }
        }
    }
}