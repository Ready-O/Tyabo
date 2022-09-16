package com.tyabo.designsystem.components.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ElevatedButton(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector?,
    onClick: () -> Unit,
    text: @Composable (() -> Unit),
){
    ElevatedButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ){
            if (leadingIcon != null){
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            text()
        }
    }
}