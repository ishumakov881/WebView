package com.knopka.kz.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ClickableViewAccessibility")
@Composable
fun WebViewScreen(url: String) {
    var isLoading by remember { mutableStateOf(false) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    val state = rememberPullToRefreshState()
    
    if (state.isRefreshing) {
        isLoading = true
        webView?.reload()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(state.nestedScrollConnection)
    ) {
        Column {
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            AndroidView(
                factory = { context ->
                    WebViewCache.get(url, context, onLoadingChange = { 
                        isLoading = it
                        if (!it) state.endRefresh()
                    }).also {
                        webView = it
                        val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
                        var startY = 0f
                        var startX = 0f
                        
                        it.setOnTouchListener { v, event ->
                            when (event.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    startY = event.y
                                    startX = event.x
                                }
                                MotionEvent.ACTION_MOVE -> {
                                    val deltaY = event.y - startY
                                    val deltaX = abs(event.x - startX)

                                    if (deltaY > touchSlop && deltaX < touchSlop && it.scrollY == 0) {
                                        v.parent.requestDisallowInterceptTouchEvent(false)
                                        return@setOnTouchListener false
                                    }
                                }
                            }
                            v.parent.requestDisallowInterceptTouchEvent(true)
                            return@setOnTouchListener false
                        }
                        
                        if (it.progress == 100) {
                            isLoading = false
                            state.endRefresh()
                        }
                    }
                },
                update = { view ->
                    if (view.url != url) {
                        view.loadUrl(url)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = state
        )
    }
}

object WebViewCache {
    private val cache = mutableMapOf<String, WebView>()
    
    fun get(url: String, context: android.content.Context, onLoadingChange: (Boolean) -> Unit): WebView {
        return cache.getOrPut(url) {
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                }
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        onLoadingChange(true)
                    }
                    
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onLoadingChange(false)
                    }
                }
                loadUrl(url)
            }
        }.also { webView ->
            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    onLoadingChange(true)
                }
                
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    onLoadingChange(false)
                }
            }
        }
    }
    
    fun clear() {
        cache.values.forEach { webView ->
            webView.clearCache(true)
            webView.destroy()
        }
        cache.clear()
    }
} 