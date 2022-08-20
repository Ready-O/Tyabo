package com.tyabo.chef.catalog.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

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
    Row() {
        TextField(value = name.value, onValueChange = { name.value = it })
        IconButton(
            colors = IconButtonDefaults.filledIconButtonColors(),
            onClick = {
                keyboardController?.hide()
                cancel()
            }
        ){}
        IconButton(
            colors = IconButtonDefaults.filledIconButtonColors(),
            onClick = {
                keyboardController?.hide()
                editCollection(name.value)
            }
        ){}
    }
}