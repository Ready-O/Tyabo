package com.tyabo.chef.editmenu.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tyabo.chef.editmenu.YoutubeVideoState
import com.tyabo.designsystem.components.LoadingBox
import com.tyabo.designsystem.components.YoutubeVideo
import com.tyabo.designsystem.components.buttons.FilledTonalButton

@Composable
fun VideoScreen(
    videoState: YoutubeVideoState,
    addYoutubeVideo: () -> Unit,
    deleteVideo: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "Social Media",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        when (videoState){
            is YoutubeVideoState.Loading -> LoadingBox()
            is YoutubeVideoState.Empty -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilledTonalButton(
                        leadingIcon = null,
                        onClick= addYoutubeVideo
                    ){
                        Text(text = "Add Youtube Link")
                    }
                }
            }
            is YoutubeVideoState.Video -> {
                YoutubeVideo(
                    title = videoState.title,
                    thumbnailUrl = videoState.thumbnailUrl,
                    videoUrl = videoState.videoUrl,
                    deleteContent = deleteVideo
                )
            }
        }
    }
}