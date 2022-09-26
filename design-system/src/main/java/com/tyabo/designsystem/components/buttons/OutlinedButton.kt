package com.tyabo.designsystem.components.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedButton(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector?,
    onClick: () -> Unit,
    text: @Composable (() -> Unit),
){
    OutlinedButton(
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