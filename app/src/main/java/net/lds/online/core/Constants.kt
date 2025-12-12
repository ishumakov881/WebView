package net.lds.online.core

object Constants {
    //const val BASE_URL = "https://lk0.lds.online"
    const val BASE_URL = "https://google.com"

    
    object Features {
        const val PULL_TO_REFRESH_ENABLED = true
        const val CUSTOM_PROGRESS_BAR_ENABLED = true
        const val LOADING_ANIMATION_ENABLED = true
        const val JAVASCRIPT_ENABLED = true
        const val DOM_STORAGE_ENABLED = true
        const val DATABASE_ENABLED = true
        const val SEARCH_ENABLED = true
        const val SCROLL_TO_TOP_ENABLED = true
        const val DARK_THEME_ENABLED = true
    }
    
    object Animation {
        const val PROGRESS_BAR_ANIMATION_DURATION = 300
        const val LOADING_ANIMATION_DURATION = 1000
        const val SCROLL_TO_TOP_THRESHOLD = 1000 // Показывать кнопку после прокрутки на 1000px
        const val SEARCH_HIGHLIGHT_DURATION = 300
    }
    
    object Search {
        const val SEARCH_DELAY = 500L // Задержка перед поиском в мс
        const val MIN_QUERY_LENGTH = 3 // Минимальная длина запроса для поиска
        const val HIGHLIGHT_COLOR = "#FFEB3B"
        const val CURRENT_HIGHLIGHT_COLOR = "#FFA000"
    }
} 