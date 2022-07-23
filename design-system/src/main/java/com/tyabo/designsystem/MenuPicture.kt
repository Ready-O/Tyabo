package com.tyabo.designsystem

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource

@Composable
fun MenuPicture(
    modifier: Modifier = Modifier,
    url: String?,
    menuname: String?,
    onClick: () -> Unit = {}
) {
    val imageModifier = modifier
        .clickable { onClick() }

    val placeholder: @Composable () -> Unit = {
        androidx.compose.foundation.Image(
            modifier = imageModifier,
            painter = painterResource(id = R.drawable.ic_photo),
            contentDescription = menuname
        )
    }
    Image(
        modifier = imageModifier,
        contentDescription = menuname,
        url = url,
        placeholder = R.drawable.ic_photo,
        loading = placeholder,
        fallback = placeholder
    )
}