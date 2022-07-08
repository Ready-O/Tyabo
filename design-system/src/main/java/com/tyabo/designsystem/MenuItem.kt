package com.tyabo.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyabo.data.CatalogItem

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    menuItem: CatalogItem.MenuItem
){
    Row(
        modifier = modifier
    ) {
        Column() {
            Text(
                modifier = Modifier,
                text = menuItem.name,
                color = Color.Black,
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier,
                text = menuItem.price,
                color = Color.DarkGray,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (menuItem.menuPictureUrl != null) {
            Box(
                modifier = Modifier.background(Color.DarkGray)
            ){
                MenuPicture(
                    modifier = Modifier.size(88.dp),
                    url = menuItem.menuPictureUrl,
                    menuname = menuItem.name
                )
            }
        }
    }
}