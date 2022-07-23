package com.tyabo.chef.editmenu.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tyabo.chef.editmenu.YoutubeVideoState
import com.tyabo.designsystem.MenuPicture

@Composable
fun VideoScreen(
    videoState: YoutubeVideoState,
    onUrlUpdate: (String) -> Unit,
    exportUrl: () -> Unit
) {
    when (videoState){
        is YoutubeVideoState.Loading -> CircularProgressIndicator()
        is YoutubeVideoState.ExportUrl -> {
            ExportUrl(
                url = videoState.url,
                onUrlUpdate = onUrlUpdate,
                exportUrl = exportUrl
            )
        }
        is YoutubeVideoState.Video -> {
            Video(
                title = videoState.title,
                thumbnailUrl = videoState.thumbnailUrl,
                videoUrl = videoState.videoUrl
            )
        }
    }
}

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

@Composable
private fun Video(
    title: String,
    thumbnailUrl: String,
    videoUrl: String
) {
    Column() {
        MenuPicture(
            url = thumbnailUrl,
            menuname = title
        )
        Text(text = title)
    }
}