package com.tyabo.designsystem

import android.text.style.ClickableSpan
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun TyaboPicture(
    modifier: Modifier = Modifier,
    url: String?,
    name: String?,
    onClick: () -> Unit = {}
) {
    val imageModifier = modifier
        .clickable { onClick() }

    val placeholder: @Composable () -> Unit = {
        androidx.compose.foundation.Image(
            modifier = imageModifier,
            painter = painterResource(id = R.drawable.ic_photo),
            contentDescription = name
        )
    }
    Image(
        modifier = imageModifier,
        contentDescription = name,
        url = url,
        placeholder = R.drawable.ic_photo,
        loading = placeholder,
        fallback = placeholder
    )
}