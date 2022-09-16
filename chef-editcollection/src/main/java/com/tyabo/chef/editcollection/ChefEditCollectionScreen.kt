package com.tyabo.chef.editcollection

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel

@ExperimentalComposeUiApi
@Composable
fun ChefEditCollectionScreen(
    navigateUp: () -> Unit,
    viewModel: ChefEditCollectionViewModel = hiltViewModel()
){

    val collectionName by viewModel.collectionState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column() {
        TextField(
            modifier = Modifier.focusRequester(focusRequester),
            value = collectionName,
            onValueChange = viewModel::onCollectionUpdate)
        Button(onClick = {
            keyboardController?.hide()
            viewModel.onCtaClicked(navigateUp)
        }){
            Text("confirm")
        }
    }
}