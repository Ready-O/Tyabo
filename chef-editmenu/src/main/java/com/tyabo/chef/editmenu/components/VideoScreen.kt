package com.tyabo.chef.editmenu.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.tyabo.chef.editmenu.YoutubeVideoState
import com.tyabo.designsystem.components.YoutubeVideo

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
    YoutubeVideo(
        title = title,
        thumbnailUrl = thumbnailUrl,
        videoUrl = videoUrl
    )
}