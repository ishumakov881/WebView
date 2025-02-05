package com.nefake.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.nefake.core.Constants
import com.nefake.ui.components.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    url: String = Constants.BASE_URL,
    onRssClick: () -> Unit,
    isDarkTheme: Boolean = false,
    modifier: Modifier = Modifier
) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    var isRefreshing by remember { mutableStateOf(false) }
    var canGoBack by remember { mutableStateOf(false) }
    var pageTitle by remember { mutableStateOf("NeFake") }
    var isSecure by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    var showScrollToTop by remember { mutableStateOf(false) }
    var scrollY by remember { mutableStateOf(0) }
    var searchMatchCount by remember { mutableStateOf(0) }
    var currentMatchIndex by remember { mutableStateOf(0) }
    var lastSearchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    fun highlightCurrentMatch() {
        webView?.evaluateJavascript("""
            (function() {
                const highlights = document.getElementsByClassName('search-highlight');
                for (let i = 0; i < highlights.length; i++) {
                    highlights[i].style.backgroundColor = '${Constants.Search.HIGHLIGHT_COLOR}';
                }
                if (highlights.length > 0) {
                    highlights[$currentMatchIndex].style.backgroundColor = '${Constants.Search.CURRENT_HIGHLIGHT_COLOR}';
                    highlights[$currentMatchIndex].scrollIntoView({ behavior: 'smooth', block: 'center' });
                }
            })()
        """.trimIndent(), null)
    }
    
    fun performSearch(query: String) {
        android.util.Log.d("WebViewSearch", "Starting search with query: $query")
        if (query.length >= Constants.Search.MIN_QUERY_LENGTH) {
            android.util.Log.d("WebViewSearch", "Query length OK, launching search")
            scope.launch {
                delay(Constants.Search.SEARCH_DELAY)
                lastSearchQuery = query
                currentMatchIndex = 0
                android.util.Log.d("WebViewSearch", "WebView instance: ${webView != null}")
                webView?.findAllAsync(query)
                android.util.Log.d("WebViewSearch", "Called findAllAsync")
                webView?.evaluateJavascript("""
                    (function() {
                        const oldHighlights = document.getElementsByClassName('search-highlight');
                        while (oldHighlights.length > 0) {
                            const parent = oldHighlights[0].parentNode;
                            parent.replaceChild(oldHighlights[0].firstChild, oldHighlights[0]);
                        }
                        
                        const walker = document.createTreeWalker(
                            document.body,
                            NodeFilter.SHOW_TEXT,
                            null,
                            false
                        );
                        
                        const regex = new RegExp('($query)', 'gi');
                        let node;
                        while (node = walker.nextNode()) {
                            if (regex.test(node.textContent)) {
                                const span = document.createElement('span');
                                span.className = 'search-highlight';
                                span.style.backgroundColor = '${Constants.Search.HIGHLIGHT_COLOR}';
                                span.style.transition = 'background-color ${Constants.Animation.SEARCH_HIGHLIGHT_DURATION}ms ease';
                                span.textContent = node.textContent;
                                node.parentNode.replaceChild(span, node);
                            }
                        }
                    })()
                """.trimIndent(), null)
            }
        } else {
            android.util.Log.d("WebViewSearch", "Query too short: ${query.length} chars")
        }
    }
    
    BackHandler(enabled = if (showSearch) true else canGoBack) {
        if (showSearch) {
            showSearch = false
            webView?.clearMatches()
        } else {
            webView?.goBack()
        }
    }
    
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = pageTitle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        if (canGoBack) {
                            MenuIcon(
                                icon = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Назад",
                                onClick = { webView?.goBack() },
                                tint = if (isDarkTheme) Color.White else Color.Black
                            )
                        }
                    },
                    actions = {
                        // RSS кнопка
                        MenuIcon(
                            icon = Icons.Default.RssFeed,
                            contentDescription = "RSS лента",
                            onClick = onRssClick,
                            tint = if (isDarkTheme) Color.White else Color.Black
                        )

                        if (Constants.Features.SEARCH_ENABLED) {
                            MenuIcon(
                                icon = Icons.Default.Search,
                                contentDescription = "Поиск",
                                onClick = { showSearch = !showSearch },
                                tint = if (isDarkTheme) Color.White else Color.Black
                            )
                        }
                        
                        MenuIcon(
                            icon = if (isSecure) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = if (isSecure) "Безопасное соединение" else "Небезопасное соединение",
                            onClick = { /* Показать информацию о безопасности */ },
                            tint = if (isSecure) Color.Green else Color.Red
                        )
                        
                        MenuIcon(
                            icon = Icons.Default.Refresh,
                            contentDescription = "Обновить",
                            onClick = { webView?.reload() },
                            tint = if (isDarkTheme) Color.White else Color.Black
                        )
                        
                        MenuIcon(
                            icon = Icons.Default.Share,
                            contentDescription = "Поделиться",
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, webView?.url ?: url)
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Поделиться"))
                            },
                            tint = if (isDarkTheme) Color.White else Color.Black
                        )
                        
                        MenuIcon(
                            icon = Icons.Default.Settings,
                            contentDescription = "Настройки",
                            onClick = { showMenu = true },
                            tint = if (isDarkTheme) Color.White else Color.Black
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = if (isDarkTheme) Color.White else Color.Black
                    )
                )
                
                if (Constants.Features.SEARCH_ENABLED) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SearchBar(
                            isVisible = showSearch,
                            onSearch = { query -> performSearch(query) },
                            onClose = { 
                                showSearch = false
                                webView?.clearMatches()
                                searchMatchCount = 0
                                currentMatchIndex = 0
                            },
                            isDarkTheme = isDarkTheme,
                            matchCount = searchMatchCount,
                            modifier = Modifier.weight(1f)
                        )
                        
                        if (showSearch && searchMatchCount > 0) {
                            SearchNavigation(
                                currentMatch = currentMatchIndex,
                                totalMatches = searchMatchCount,
                                onPrevious = {
                                    currentMatchIndex = (currentMatchIndex - 1 + searchMatchCount) % searchMatchCount
                                    highlightCurrentMatch()
                                },
                                onNext = {
                                    currentMatchIndex = (currentMatchIndex + 1) % searchMatchCount
                                    highlightCurrentMatch()
                                },
                                isDarkTheme = isDarkTheme
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            if (Constants.Features.SCROLL_TO_TOP_ENABLED) {
                ScrollToTopButton(
                    isVisible = showScrollToTop,
                    onClick = {
                        webView?.scrollTo(0, 0)
                        showScrollToTop = false
                        scrollY = 0
                    },
                    isDarkTheme = isDarkTheme
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier.padding(paddingValues)
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    isRefreshing = true
                    webView?.reload()
                },
                indicator = { state, trigger ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = trigger,
                        contentColor = if (isDarkTheme) Color.White else Color.Black
                    )
                }
            ) {
                Column {
                    if (isLoading) {
                        CustomProgressBar(progress = progress)
                    }
                    
                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                
                                webViewClient = object : WebViewClient() {
                                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                        super.onPageStarted(view, url, favicon)
                                        isLoading = true
                                        isSecure = url?.startsWith("https://") == true
                                    }
                                    
                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        isLoading = false
                                        isRefreshing = false
                                        canGoBack = view?.canGoBack() ?: false
                                        pageTitle = view?.title ?: "NeFake"
                                        isSecure = url?.startsWith("https://") == true
                                        
                                        if (isDarkTheme) {
                                            view?.evaluateJavascript("""
                                                document.body.style.backgroundColor = '#121212';
                                                document.body.style.color = '#FFFFFF';
                                                document.querySelectorAll('*').forEach(element => {
                                                    element.style.backgroundColor = '#121212';
                                                    if (element.style.color === '') {
                                                        element.style.color = '#FFFFFF';
                                                    }
                                                });
                                            """.trimIndent(), null)
                                        }
                                        
                                        // Восстанавливаем поиск после перезагрузки
                                        if (showSearch && lastSearchQuery.isNotEmpty()) {
                                            performSearch(lastSearchQuery)
                                        }
                                    }
                                }
                                
                                settings.apply {
                                    javaScriptEnabled = Constants.Features.JAVASCRIPT_ENABLED
                                    domStorageEnabled = Constants.Features.DOM_STORAGE_ENABLED
                                    databaseEnabled = Constants.Features.DATABASE_ENABLED
                                }
                                
                                setWebChromeClient(object : android.webkit.WebChromeClient() {
                                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                        progress = newProgress / 100f
                                    }
                                    
                                    override fun onReceivedTitle(view: WebView?, title: String?) {
                                        super.onReceivedTitle(view, title)
                                        pageTitle = title ?: "NeFake"
                                    }
                                })
                                
                                setFindListener { activeMatchOrdinal, numberOfMatches, isDoneCounting ->
                                    android.util.Log.d("WebViewSearch", "Find results: active=$activeMatchOrdinal, total=$numberOfMatches, done=$isDoneCounting")
                                    if (isDoneCounting) {
                                        searchMatchCount = numberOfMatches
                                        if (numberOfMatches > 0) {
                                            android.util.Log.d("WebViewSearch", "Found matches: $numberOfMatches")
                                            highlightCurrentMatch()
                                        } else {
                                            android.util.Log.d("WebViewSearch", "No matches found")
                                        }
                                    }
                                }
                                
                                setOnScrollChangeListener { _, _, scrollY, _, _ ->
                                    showScrollToTop = scrollY >= Constants.Animation.SCROLL_TO_TOP_THRESHOLD
                                }
                                
                                loadUrl(url)
                                webView = this
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            
            if (isLoading) {
                LoadingAnimation(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        
        if (showMenu) {
            AlertDialog(
                onDismissRequest = { showMenu = false },
                title = { Text("Настройки") },
                text = {
                    Column {
                        Text("Тема: ${if (isDarkTheme) "Темная" else "Светлая"}")
                        Text("JavaScript: ${if (Constants.Features.JAVASCRIPT_ENABLED) "Включен" else "Выключен"}")
                        Text("Кэширование: ${if (Constants.Features.DATABASE_ENABLED) "Включено" else "Выключено"}")
                        Text("Поиск: ${if (Constants.Features.SEARCH_ENABLED) "Включен" else "Выключен"}")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showMenu = false }) {
                        Text("Закрыть")
                    }
                }
            )
        }
    }
} 