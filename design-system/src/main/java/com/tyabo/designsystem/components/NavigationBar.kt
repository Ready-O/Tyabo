package com.tyabo.designsystem.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun NavigationBar(
    selectedRoute: String?,
    destinations: List<MainDestination>,
    navigate: (String) -> Unit
){
    androidx.compose.material3.NavigationBar(
        modifier = Modifier.height(57.dp)
    ) {
        destinations.forEach { destination ->
            val selected = selectedRoute == destination.route
            NavigationBarItem(
                selected = selected,
                onClick = { navigate(destination.route) },
                icon = {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector =
                            if (selected) destination.selectedIcon
                            else destination.unselectedIcon,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

data class MainDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)