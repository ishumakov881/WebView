package com.knopka.kz.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Notifications

// import androidx.compose.material.icons.filled.Message
// import androidx.compose.material.icons.filled.Person

//http://10.0.59.120:5173/search
sealed class Screen(val route: String, val url: String? = null) {
    //object HomeScreen : Screen("home", "https://позитивфото.рф")
    object HomeScreen : Screen("home", "https://xn--b1ajda1aeab4adu.xn--p1ai")

//    object AddItem : Screen("add_item", "https://knopka.kz/item/add")
//    object Messages : Screen("messages", "https://knopka.kz/cabinet/messages")
//    object Profile : Screen("profile", "https://knopka.kz/cabinet/items")
    //https://youtube.com

    //https://fex.net/ru/s/nb1aona

//    object Home : Screen("home", "https://fex.net/ru/")
//    object AddItem : Screen("add_item", "https://ps.uci.edu/~franklin/doc/file_upload.html")
//    object Messages : Screen("messages", "https://www.csm-testcenter.org/test?do=show&subdo=common&test=file_upload")
//    object Profile : Screen("profile", "https://www.azurespeed.com/Azure/UploadLargeFile")

//    object Home : Screen("home", "https://fex.net/ru/")
//    object AddItem : Screen("add_item", "https://webcammictest.com/")
//    object Messages : Screen(
//        "messages",
//        "https://www.onlinemictest.com/webcam-test/"
//    )
//    object Profile : Screen("profile", "https://restream.io/tools/webcam-test")
//https://jumpshare.com/webcam-test https://webcamtests.com/

    //https://knopka.kz/cabinet/favs

    // Для других экранов (url не нужен)
    object NotificationsScreen : Screen("notifications")
    object ProfileScreen : Screen("profile")

    data class BottomNavItem(
        val screen: Screen,
        val label: String,
        val icon: ImageVector
    )

    companion object {
        // СТАРАЯ ВЕРСИЯ (оставляем для истории)
        /*
        val bottomNavItems = listOf(
            BottomNavItem(Home, "Главная", Icons.Filled.Home),
            BottomNavItem(AddItem, "Добавить", Icons.Filled.Addchart),
            // BottomNavItem(Messages, "Сообщения", Icons.Filled.Message),
            // BottomNavItem(Profile, "Кабинет", Icons.Filled.Person)
        )
        */

        // НОВАЯ ВЕРСИЯ (по иконкам с твоего скрина)
        val bottomNavItems = listOf(
            BottomNavItem(HomeScreen, "Главная", Icons.Filled.Home),
            BottomNavItem(NotificationsScreen, "Уведомления", Icons.Filled.Notifications),
            BottomNavItem(ProfileScreen, "Меню", Icons.Filled.Apps)
            // Если нужны другие экраны — раскомментируй или добавь!
        )
    }
} 