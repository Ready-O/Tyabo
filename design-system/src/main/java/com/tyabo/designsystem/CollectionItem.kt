package com.tyabo.designsystem

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CollectionItem(
    modifier: Modifier = Modifier,
    collectionName: String,
){
    Text(
        text = collectionName,
        style = MaterialTheme.typography.headlineMedium,
    )
}