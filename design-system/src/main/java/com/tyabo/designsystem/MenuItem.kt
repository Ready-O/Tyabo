package com.tyabo.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tyabo.data.CatalogItem
import com.tyabo.designsystem.picture.StandardPicture

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    menuItem: CatalogItem.MenuItem,
    onClick: () -> Unit
){
    Row(
        modifier = modifier
            .clickable { onClick() },
    ) {
        Column() {
            Text(
                modifier = Modifier,
                text = menuItem.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Light
            )
            Text(
                modifier = Modifier,
                text = menuItem.price,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light
            )
            if (menuItem.description.isNotEmpty()){
                Text(
                    modifier = Modifier,
                    text = menuItem.description,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        if (menuItem.menuPictureUrl != null) {
            Box(
                modifier = Modifier.background(Color.DarkGray)
            ){
                StandardPicture(
                    modifier = Modifier.size(102.dp),
                    url = menuItem.menuPictureUrl,
                    name = menuItem.name
                )
            }
        }
    }
}