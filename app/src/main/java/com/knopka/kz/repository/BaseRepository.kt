package com.knopka.kz.repository

import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.coroutines.CoroutineContext

abstract class BaseRepository(private val coroutineContext: CoroutineContext) {

    protected open fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    fun extractUrlFromResponse(responseBody: String): String? {
        val regex = """https?://[^\s]+""".toRegex() // Регулярное выражение для поиска ссылок
        val matchResult = regex.find(responseBody) // Ищем первое совпадение
        return matchResult?.value // Возвращаем найденную ссылку
    }

    protected suspend fun <T> executeRequest(request: Request, transform: (String) -> T): T? {
        return withContext(coroutineContext) {
            try {
                val client = createOkHttpClient()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: return@withContext null
                    transform(body)
                } else {
                    throw IOException("Unexpected code $response")
                }
            } catch (e: Exception) {
                println("Error during request: ${e.message}")
                null
            }
        }
    }
}