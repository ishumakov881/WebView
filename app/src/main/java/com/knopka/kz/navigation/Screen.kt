package com.knopka.kz.navigation

//http://10.0.59.120:5173/search
sealed class Screen(val route: String, val url: String) {
    object Home : Screen("home", "https://fhd1080apk.cyou")
    //object Home : Screen("home", "file:///android_asset/viewport.html")


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

    companion object {
        val bottomNavItems = listOf(
            Triple(Home, "Главная", "home"),
//            Triple(AddItem, "Добавить", "addchart"),
//            Triple(Messages, "Сообщения", "message"),
//            Triple(Profile, "Кабинет", "kabinet")
        )
    }
} 