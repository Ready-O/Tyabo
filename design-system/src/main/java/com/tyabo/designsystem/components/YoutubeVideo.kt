package com.tyabo.designsystem.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.tyabo.designsystem.picture.StandardImage

@Composable
fun YoutubeVideo(
    title: String,
    thumbnailUrl: String,
    videoUrl: String
){
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
    Column() {
        StandardImage(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { ContextCompat.startActivity(context, intent, null) },
            url = thumbnailUrl,
            fallback = null
        )
        Text(text = title)
    }
}