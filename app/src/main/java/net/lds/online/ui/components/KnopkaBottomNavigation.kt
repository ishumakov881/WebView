package net.lds.online.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Addchart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import net.lds.online.navigation.Screen

@Composable
fun KnopkaBottomNavigation(
    currentRoute: String,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar {
        Screen.bottomNavItems.forEach { (screen, title, icon) ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = getIcon(icon),
                        contentDescription = title
                    )
                },
                label = { Text(text = title) }
            )
        }
    }
}

//private fun getDrawableResource(name: String): Int {
//    return when (name) {
//        "home" -> R.drawable.home
//        "addchart" -> R.drawable.addchart
//        "message" -> R.drawable.message
//        "kabinet" -> R.drawable.kabinet
//        else -> throw IllegalArgumentException("Unknown icon name: $name")
//    }
//}

private fun getIcon(name: String): ImageVector {
    return when (name) {
        "home" -> Icons.Default.Home
        "addchart" -> Icons.Default.Addchart
        "message" -> Icons.AutoMirrored.Filled.Message
        "kabinet" -> Icons.Default.Person
        else -> throw IllegalArgumentException("Unknown icon name: $name")
    }
}