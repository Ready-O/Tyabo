package com.tyabo.designsystem

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun YoutubeVideo(
    title: String,
    thumbnailUrl: String,
    videoUrl: String
){
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
    Column() {
        ClickablePicture(
            url = thumbnailUrl,
            name = title,
            onClick = {
                ContextCompat.startActivity(context, intent, null)
            }
        )
        Text(text = title)
    }
}