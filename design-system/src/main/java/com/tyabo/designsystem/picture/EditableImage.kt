package com.tyabo.designsystem.picture

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.options

@Composable
fun EditableImage(
    modifier: Modifier = Modifier,
    backgroundModifier: Modifier = Modifier,
    imageUrl: String? = null,
    close: (() -> Unit)? = null,
    centerButton: @Composable ((Modifier) -> Unit)? = null,
) {
    Box(modifier = backgroundModifier){
        if (imageUrl != null){
            StandardImage(
                modifier = modifier,
                url = imageUrl,
                fallback = null,
            )
        }
        if (close != null){
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp,end = 4.dp),
                onClick = close
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                )
            }
        }
        if (centerButton != null) {
            centerButton(
                Modifier.align(Alignment.Center)
            )
        }
    }
}