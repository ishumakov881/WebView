package com.walhalla.webview

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat

class ActivityUtils {
    companion object {

        fun openBrowser(context: Context, url: String) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Browser not found", Toast.LENGTH_SHORT).show()
            }
        }

        fun starttg(context: Context, url: String) {
            try {
                // Проверяем, является ли ссылка корректной
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)

                // Проверяем, есть ли приложение Telegram на устройстве
                if (intent.resolveActivity(context.packageManager) != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } else {
                    // Если Telegram не установлен, открываем ссылку в браузере
                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/${uri.host}"))
                    webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(webIntent)
                }
            } catch (e: Exception) {
                // Обработка ошибок (например, некорректная ссылка)
                Toast.makeText(
                    context,
                    "Ошибка при открытии ссылки: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        fun startEmailActivity(context: Context, to: String?, subject: String?, body: String?) {

        }

        fun startCallActivity(context: Context, url: String) {

        }

        fun startSmsActivity(context: Context, url: String) {

        }

        fun startMapSearchActivity(context: Context, url: String) {

        }

        fun startMapYandex(context: Context, replace: String) {

        }

        fun startyandexnavi(context: Context, url: String) {

        }

        fun market(context: Context, url: String) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Приложение Play Market не найдено", Toast.LENGTH_SHORT)
                    .show()
            }
        }



        fun handleIntentUrl(context: Context, url: String) {
            try {
                val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                val packageManager = context.packageManager

                // Проверяем, есть ли приложение для обработки
                val activities = packageManager.queryIntentActivities(intent, 0)
                if (activities.isNotEmpty()) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent) // Запускаем, если доступно
                } else {
                    // Если нет, пробуем открыть в Play Market
                    intent.`package`?.let { packageName ->
                        val marketUrl = "market://details?id=$packageName"
                        ActivityUtils.market(context, marketUrl)
                    } ?: run {
                        Toast.makeText(context, "Приложение не найдено", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: android.content.ActivityNotFoundException) {
                e.printStackTrace()
                Toast.makeText(context, "Ошибка обработки ссылки", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Ошибка обработки ссылки", Toast.LENGTH_SHORT).show()
            }
        }

//        fun whatsapp(context: Context, url: String) {
//            try {
//                val intent = Intent(Intent.ACTION_VIEW).apply {
//                    data = Uri.parse(url)
//                    setPackage("com.whatsapp") // Указываем пакет WhatsApp
//                }
//                ContextCompat.startActivity(context, intent, null)
//            } catch (e: ActivityNotFoundException) {
//                Toast.makeText(context, "WhatsApp не установлен", Toast.LENGTH_SHORT).show()
//            }
//        }


        private val appPackages = mapOf(
            "whatsapp" to listOf("com.whatsapp"),
            "viber" to listOf("com.viber.voip"),
            "tg" to listOf("org.telegram.messenger", "org.thunderdog.challegram"),
            "skype" to listOf("com.skype.raider"),
            "yandexnavi" to listOf("ru.yandex.yandexnavi", "ru.yandex.maps")
        )

        fun resolveUrl(context: Context, url: String) {
            val scheme = url.substringBefore(":") // Получаем схему (например, "whatsapp", "tg", "https")

            // Игнорируем стандартные URL (http, https)
            if (scheme == "http" || scheme == "https") return

            val packages = appPackages[scheme] ?: emptyList()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            if (packages.isNotEmpty()) {
                for (packageName in packages) {
                    if (isAppInstalled(context, packageName)) {
                        intent.setPackage(packageName) // Используем первый найденный пакет
                        break
                    }
                }
            }

            try {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ContextCompat.startActivity(context, intent, null)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Нет подходящего приложения для ${scheme}", Toast.LENGTH_SHORT).show()
            }
        }

        private fun isAppInstalled(context: Context, packageName: String): Boolean {
            return try {
                context.packageManager.getPackageInfo(packageName, 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }

        fun openMessagingApp(context: Context, url: String, packageName: String) {
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url)
                    setPackage(packageName) // Открываем нужное приложение
                }
                ContextCompat.startActivity(context, intent, null)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Приложение не установлено: $packageName", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
