package com.knopka.kz.repository

import kotlinx.coroutines.Dispatchers

class LocalRepository : BaseRepository(Dispatchers.IO) {

    val url = "http://10.0.20.179:5173/search".toCharArray()

    fun decodeCode(url: CharArray): String {
        return String(url)
    }

    suspend fun fetchUrlFromGoogleDrive(): String {
        return decodeCode(url)
    }
}