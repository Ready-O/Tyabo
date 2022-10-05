package com.tyabo.feature.chef.editmenu.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YoutubeScreen(
    youtubeUrl: String,
    onUrlUpdate: (String) -> Unit,
    backToMain: () -> Unit,
    exportUrl: () -> Unit
){
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            VideoTopBar(
                close = backToMain,
                onCtaClicked = exportUrl
            )
        }
    ) { paddingForBars ->
        Column(
            modifier = Modifier.padding(paddingForBars)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = youtubeUrl,
                onValueChange = onUrlUpdate,
                label = { Text("YoutubeUrl") }
            )
        }
    }
}
