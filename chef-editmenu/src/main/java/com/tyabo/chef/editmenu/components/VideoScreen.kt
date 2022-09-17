package com.tyabo.chef.editmenu.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.tyabo.chef.editmenu.YoutubeVideoState
import com.tyabo.designsystem.components.YoutubeVideo

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VideoScreen(
    videoState: YoutubeVideoState,
    onUrlUpdate: (String) -> Unit,
    exportUrl: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    when (videoState){
        is YoutubeVideoState.Loading -> CircularProgressIndicator()
        is YoutubeVideoState.ExportUrl -> {
            ExportUrl(
                url = videoState.url,
                onUrlUpdate = onUrlUpdate,
                exportUrl = {
                    keyboardController?.hide()
                    exportUrl()
                }
            )
        }
        is YoutubeVideoState.Video -> {
            YoutubeVideo(
                title = videoState.title,
                thumbnailUrl = videoState.thumbnailUrl,
                videoUrl = videoState.videoUrl
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExportUrl(
    url: String,
    onUrlUpdate: (String) -> Unit,
    exportUrl: () -> Unit
) {
    Row() {
        TextField(value = url, onValueChange = onUrlUpdate)
        Button(onClick = exportUrl) {
            Text(text = "Valider")
        }
    }
}