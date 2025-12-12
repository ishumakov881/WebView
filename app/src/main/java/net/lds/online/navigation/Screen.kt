package net.lds.online.navigation

sealed class Screen(val route: String, val url: String) {
    object Home : Screen("home", "https://lk0.lds.online")
//    object AddItem : Screen("add_item", "/item/add")
//    object Messages : Screen("messages", "/cabinet/messages")
//    object Profile : Screen("profile", "/cabinet/items")
    //https://youtube.com

    ///ru/s/nb1aona

//    object Home : Screen("home", "/ru/")
//    object AddItem : Screen("add_item", "https://ps.uci.edu/~franklin/doc/file_upload.html")
//    object Messages : Screen("messages", "https://www.csm-testcenter.org/test?do=show&subdo=common&test=file_upload")
//    object Profile : Screen("profile", "https://www.azurespeed.com/Azure/UploadLargeFile")

//    object Home : Screen("home", "/ru/")
//    object AddItem : Screen("add_item", "https://webcammictest.com/")
//    object Messages : Screen(
//        "messages",
//        "https://www.onlinemictest.com/webcam-test/"
//    )
//    object Profile : Screen("profile", "https://restream.io/tools/webcam-test")
//https://jumpshare.com/webcam-test https://webcamtests.com/

    ///cabinet/favs

    companion object {
        val bottomNavItems = listOf(
            Triple(Home, "Главная", "home"),
//            Triple(AddItem, "Добавить", "addchart"),
//            Triple(Messages, "Сообщения", "message"),
//            Triple(Profile, "Кабинет", "kabinet")
        )
    }
} 