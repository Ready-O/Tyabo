package com.tyabo.chef.editmenu.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.tyabo.designsystem.picture.EditableImage

@Composable
fun MenuPicture(
    menuProfileUrl: String?,
    onPictureUpdate: (String?) -> Unit
) {
    Box(
        modifier = Modifier.background(Color.LightGray)
    ) {
        EditableImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            pictureUrl = menuProfileUrl,
            onClick = onPictureUpdate
        )
    }
}