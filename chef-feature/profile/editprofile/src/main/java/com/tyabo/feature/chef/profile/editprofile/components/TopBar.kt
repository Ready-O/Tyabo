package com.tyabo.feature.chef.profile.editprofile.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun TopBar(
    navigateUp: () -> Unit,
    onCtaClicked: () -> Unit
){

    val keyboardController = LocalSoftwareKeyboardController.current

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null
                )
            }
        },
        title = { Text("Edit Profile") },
        actions = {
            Button(
                modifier = Modifier.padding(horizontal = 12.dp),
                onClick = {
                    keyboardController?.hide()
                    onCtaClicked()
                }
            ) {
                Text("Confirm")
            }
        }
    )
}