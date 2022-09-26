package com.tyabo.chef.editcollection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class,ExperimentalComposeUiApi::class)
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

    Column {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = navigateUp){
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null
                    )
                }
            },
            title = { Text("Edit Collection") },
            actions = {
                Button(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    onClick = {
                        keyboardController?.hide()
                        viewModel.onCtaClicked(navigateUp)
                    }
                ){
                    Text("Confirm")
                }
            }
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = collectionName,
                onValueChange = viewModel::onCollectionUpdate,
                label = { Text("Collection Name") }
            )
        }
    }
}