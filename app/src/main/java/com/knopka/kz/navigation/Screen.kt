package com.knopka.kz.navigation
//http://10.0.59.120:5173/search
sealed class Screen(val route: String, val url: String) {
    object Home : Screen("home", "https://knopka.kz/")
    object AddItem : Screen("add_item", "https://knopka.kz/item/add")
    object Messages : Screen("messages", "https://knopka.kz/cabinet/messages")
    object Profile : Screen("profile", "https://knopka.kz/cabinet/items")

    //https://knopka.kz/cabinet/favs

    companion object {
        val bottomNavItems = listOf(
            Triple(Home, "Главная", "home"),
            Triple(AddItem, "Добавить", "addchart"),
            Triple(Messages, "Сообщения", "message"),
            Triple(Profile, "Кабинет", "kabinet")
        )
    }
} 