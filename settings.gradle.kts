enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        flatDir {
            dirs("D:\\walhalla\\sdk\\android\\JetpackCompose\\WebViewKnopkakz0\\libs")
        }
        google()
//        google {
//            mavenContent {
//                includeGroupAndSubgroups("androidx")
//                includeGroupAndSubgroups("com.android")
//                includeGroupAndSubgroups("com.google")
//            }
//        }
        //mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
        jcenter()
    }
}
dependencyResolutionManagement {
    repositories {
        flatDir {
            dirs("D:\\walhalla\\sdk\\android\\JetpackCompose\\WebViewKnopkakz0\\libs")
        }
        google()
//        google {
//            mavenContent {
//                includeGroupAndSubgroups("androidx")
//                includeGroupAndSubgroups("com.android")
//                includeGroupAndSubgroups("com.google")
//            }
//        }
        //mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://repo1.maven.org/maven2/")
        maven("https://androidx.dev/storage/compose-compiler/repository/")

        maven("https://maven.google.com")
        maven("https://dl.bintray.com/videolan/Android")
    }
}


rootProject.name = "WebView"
include(":app")
include(":wvcore")
include(":wvrss")

include(":features:webview")
project(":features:webview").projectDir = File("C:\\src\\Synced/WalhallaUI\\features\\webview")
