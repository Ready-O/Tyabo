package com.tyabo.feature.chef.editmenu.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.tyabo.designsystem.components.buttons.OutlinedButton
import com.tyabo.designsystem.picture.EditableImage
import com.tyabo.designsystem.picture.InputImage

@Composable
fun MenuPicture(
    menuProfileUrl: String?,
    onPictureUpdate: (String?) -> Unit
) {
    if (menuProfileUrl == null){
        InputImage(inputAction = onPictureUpdate) { input ->
            EditableImage(
                backgroundModifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .fillMaxWidth()
                    .height(150.dp),
            ) { modifier ->
                OutlinedButton(
                    modifier = modifier,
                    leadingIcon = Icons.Filled.Add,
                    onClick = input
                ) {
                    Text(text = "Add Picture")
                }
            }
        }
    }
    else {
        EditableImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            backgroundModifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
            imageUrl = menuProfileUrl,
            close = { onPictureUpdate(null) }
        )
    }
}