//package com.knopka.kz.repository
//
//import kotlinx.coroutines.Dispatchers
//import okhttp3.Request
//
//class GoogleDriveRepository : BaseRepository(Dispatchers.IO) {
//
//    private val googleDriveUrl = "https://drive.google.com/uc?id=1GFR8u1gEUt3QUFaVdhuND3kb2HiQW5e0"
//
//    suspend fun fetchUrlFromGoogleDrive(): String? {
//        val request = Request.Builder()
//            .url(googleDriveUrl)
//            .build()
//
//        return executeRequest(request) { responseBody ->
//            try {
//                val jsonObject = responseBody
//                jsonObject
//            } catch (e: Exception) {
//                println("Error parsing JSON: ${e.message}")
//                null
//            }
//        }
//    }
//}