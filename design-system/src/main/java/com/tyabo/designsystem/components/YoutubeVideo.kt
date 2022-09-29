package com.tyabo.designsystem.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tyabo.designsystem.picture.EditableImage
import com.tyabo.designsystem.picture.StandardImage

@Composable
fun YoutubeVideo(
    title: String,
    thumbnailUrl: String,
    videoUrl: String,
    deleteContent: () -> Unit
){
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
    Column {
        EditableImage(
            modifier = Modifier.fillMaxWidth().height(190.dp),
            imageUrl = thumbnailUrl,
            close = deleteContent
        ){ modifier ->
            IconButton(
                modifier = modifier.size(50.dp),
                onClick = { ContextCompat.startActivity(context, intent, null) }
            ) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                )
            }
        }
        Text(text = title)
    }
}