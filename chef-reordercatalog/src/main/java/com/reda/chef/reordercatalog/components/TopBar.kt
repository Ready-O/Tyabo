package com.reda.chef.reordercatalog.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBar(
    navigateUp: () -> Unit,
    onCtaClicked: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        title = { Text("Reorder") },
        actions = {
            Button(
                modifier = Modifier.padding(horizontal = 12.dp),
                onClick = onCtaClicked
            ) {
                Text("Confirm")
            }
        }
    )
}