package com.tyabo.feature.chef.profile.editprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.tyabo.designsystem.components.buttons.OutlinedButton
import com.tyabo.designsystem.components.buttons.TextButton
import com.tyabo.designsystem.picture.EditableImage
import com.tyabo.designsystem.picture.InputImage
import com.tyabo.designsystem.picture.StandardImage

@Composable
fun BusinessPicture(
    modifier: Modifier = Modifier,
    businessProfileUrl: String?,
    onPictureUpdate: (String?) -> Unit
) {
    if (businessProfileUrl == null){
        InputImage(inputAction = onPictureUpdate) { input ->
            EditableImage(
                backgroundModifier = modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .fillMaxWidth()
                    .height(150.dp),
            ) { modifier ->
                OutlinedButton(
                    modifier = modifier,
                    leadingIcon = Icons.Filled.Add,
                    onClick = input
                ) {
                    Text(text = "Add Business Picture")
                }
            }
        }
    }
    else {
        EditableImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            backgroundModifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
            imageUrl = businessProfileUrl,
            close = { onPictureUpdate(null) }
        )
    }
}

@Composable
fun ChefPicture(
    modifier: Modifier = Modifier,
    chefProfileUrl: String?,
    onPictureUpdate: (String?) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        StandardImage(
            modifier = modifier
                .size(100.dp)
                .clip(CircleShape),
            url = chefProfileUrl,
            fallback = null
        )
        if (chefProfileUrl == null){
            InputImage(inputAction = onPictureUpdate) { input ->
                Row() {
                    TextButton(leadingIcon = null, onClick = input) {
                        Text(text = "Change Chef Photo")
                    }
                }
            }
        }
        else{
            Row() {
                TextButton(
                    leadingIcon = null,
                    onClick = { onPictureUpdate(null) }
                ) {
                    Text(text = "Delete Chef Photo")
                }
            }
        }
    }
}