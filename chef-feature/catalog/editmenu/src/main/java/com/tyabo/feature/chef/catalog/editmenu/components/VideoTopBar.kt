package com.tyabo.feature.chef.catalog.editmenu.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun VideoTopBar(
    close: () -> Unit,
    onCtaClicked: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    BackHandler() {
        keyboardController?.hide()
        close()
    }

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                keyboardController?.hide()
                close()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        title = { },
        actions = {
            Button(
                modifier = Modifier.padding(horizontal = 12.dp),
                onClick = {
                    keyboardController?.hide()
                    onCtaClicked()
                }
            ) {
                Text("Add Video")
            }
        }
    )
}