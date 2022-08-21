package com.tyabo.designsystem.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TextButton(
    leadingIcon: ImageVector,
    onClick: () -> Unit,
    text: @Composable (() -> Unit),
){
    TextButton(
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            text()
        }
    }
}