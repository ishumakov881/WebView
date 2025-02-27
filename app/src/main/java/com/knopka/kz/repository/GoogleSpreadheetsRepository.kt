package com.knopka.kz.repository

import kotlinx.coroutines.Dispatchers
import okhttp3.Request

class GoogleSpreadheetsRepository : BaseRepository(Dispatchers.IO) {

    //val url = "https://docs.google.com/spreadsheets/d/e/2PACX-1vQKFNb6bCwxzY0SQ5l_fENfzo82WQ9K85yz8pg-98AmN5CX3gT8M7H8XcMVvpcuduTkKjlOwfUzX6MZ/pub?gid=0&single=true&output=csv"


    val key = "2PACX-1vQ7Lpje68Wu-LZUAzGAVTfcAgJK-OBcurrzQZoSpHHMvOkGVm8oXDVJHikaxlGuyWQxY0Xhx6gH0vby".toCharArray()
    val part0 = "https://docs.google.com/spreadsheets/d/e/".toCharArray()
    val part1 = "/pub?output=csv".toCharArray()

    fun decodeCode(key: CharArray, part0: CharArray, part1: CharArray): String {
        return String(part0) + String(key) + String(part1)
    }

    suspend fun fetchUrlFromGoogleDrive(): String? {

        val url = decodeCode(key, part0, part1)

        val request = Request.Builder()
            .url(url)
            .build()

        return executeRequest(request) { responseBody ->
            // Преобразуем JSON-ответ в строку URL
            try {
                extractUrlFromResponse(responseBody)
            } catch (e: Exception) {
                println("Error parsing JSON: ${e.message}")
                null
            }
        }
    }
}