package com.tyabo.designsystem.picture

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.tyabo.designsystem.R

@Composable
fun StandardPicture(
    modifier: Modifier = Modifier,
    url: String?,
    name: String?,
) {

    val placeholder: @Composable () -> Unit = {
        androidx.compose.foundation.Image(
            modifier = modifier,
            painter = painterResource(id = R.drawable.ic_photo),
            contentDescription = name
        )
    }
    Image(
        modifier = modifier,
        contentDescription = name,
        url = url,
        placeholder = R.drawable.ic_photo,
        loading = placeholder,
        fallback = placeholder
    )
}