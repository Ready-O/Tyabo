package com.tyabo.designsystem.picture

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.tyabo.designsystem.R

@Composable
fun StandardImage(
    modifier: Modifier = Modifier,
    url: String?,
    fallback: Painter?
) {
    AsyncImage(
        modifier = modifier,
        model = url,
        fallback = fallback ?: painterResource(id = R.drawable.ic_photo),
        placeholder = painterResource(id = R.drawable.ic_photo),
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}