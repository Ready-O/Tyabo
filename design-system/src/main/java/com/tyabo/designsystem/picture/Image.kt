package com.tyabo.designsystem.picture

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter

@OptIn(ExperimentalCoilApi::class)
@Composable
fun Image(
    modifier: Modifier = Modifier,
    url: String?,
    contentDescription: String? = null,
    crossfade: Boolean = true,
    contentScale: ContentScale = ContentScale.Crop,
    loading: @Composable () -> Unit = { DefaultImage(modifier) },
    fallback: @Composable () -> Unit = { DefaultImage(modifier) },
    @DrawableRes placeholder: Int? = null
) {
    if (url == null) fallback()
    else {
        val painter = rememberImagePainter(data = url, builder = {
            this.crossfade(crossfade)
            this.bitmapConfig(Bitmap.Config.ARGB_8888)
            if (placeholder != null) this.placeholder(placeholder)
        })
        when (painter.state) {
            is ImagePainter.State.Loading -> loading()
            is ImagePainter.State.Error -> fallback()
            else -> androidx.compose.foundation.Image(
                modifier = modifier,
                contentScale = contentScale,
                painter = painter,
                contentDescription = contentDescription,
            )
        }
    }
}

@Composable
fun DefaultImage(modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(Color.Green))
}