### **Техническое Задание (ТЗ) для `CustomWebViewClient`**

#### **1. Цель**
Обеспечить надежную и удобную для пользователя обработку состояний загрузки страниц и ошибок в WebView. `CustomWebViewClient` должен плавно управлять переходами между контентом, состоянием загрузки и кастомным экраном ошибки, предотвращая мерцание или отображение нативных страниц ошибок WebView.

#### **2. Ключевые компоненты**
*   **`CustomWebViewClient`**: Основной класс, отвечающий за обработку событий навигации и ошибок WebView.
*   **`ChromeView` (интерфейс)**: Мост для связи между `CustomWebViewClient` и слоем UI (экранами на Jetpack Compose).
*   **`WebUiState` (Sealed Class)**: Класс, представляющий состояние UI для WebView, как минимум с двумя состояниями: `Content` (Контент) и `Error` (Ошибка).
*   **Кастомный экран ошибки**: Composable-функция, которая отображается пользователю при сбое загрузки веб-страницы.

#### **3. Функциональные требования**

*   **3.1. Отслеживание состояния**
    *   `CustomWebViewClient` должен поддерживать приватный флаг `pageHasError` (тип `Boolean`).
    *   Этот флаг должен сбрасываться в `false` в начале каждой новой навигации (то есть в методе `onPageStarted`).
    *   Этот флаг должен устанавливаться в `true` при каждом обнаружении ошибки загрузки (то есть во всех переопределениях `onReceivedError`).

*   **3.2. Отображение ошибки**
    *   При обнаружении любой ошибки загрузки (`onReceivedError`) `CustomWebViewClient` должен немедленно инициировать показ кастомного экрана ошибки.
    *   Это достигается путем вызова метода интерфейса `ChromeView` (например, `setErrorPage(error)`), который обновляет состояние UI.
    *   Внутреннее состояние `uiState` должно быть установлено в `WebUiState.Error(error)`.

*   **3.3. Логика скрытия экрана ошибки (Ключевое требование)**
    *   Кастомный экран ошибки **не должен** скрываться в начале загрузки новой страницы (`onPageStarted`). Он должен оставаться видимым, чтобы пользователь не увидел нативную страницу ошибки WebView или пустой экран.
    *   **Для Android API 23 (Marshmallow) и выше:** Экран ошибки должен скрываться только тогда, когда WebView начинает отрисовывать контент новой страницы. Это должно быть реализовано в методе `onPageCommitVisible`. Вызов этого метода является надежным индикатором успешной загрузки.
    *   **Для Android API 22 (Lollipop MR1) и ниже:** Поскольку `onPageCommitVisible` недоступен, в качестве запасного варианта будет использоваться `onPageFinished`. Экран ошибки будет скрыт в `onPageFinished` только в том случае, если флаг `pageHasError` для текущей загрузки страницы имеет значение `false`.
    *   Внутри методов, ответственных за скрытие экрана ошибки (`onPageCommitVisible` и `onPageFinished`), `uiState` должен быть сброшен в `WebUiState.Content`, и должен быть вызван соответствующий метод интерфейса `ChromeView` (например, `removeErrorPage()`).

*   **3.4. Ответственность `onPageFinished`**
    *   Метод `onPageFinished` **не должен** быть основным способом скрытия экранов ошибок на современных API. Его роль сводится к обеспечению обратной совместимости для старых API и выполнению других задач, связанных с завершением загрузки (например, остановка индикаторов загрузки).

#### **4. Сценарий работы: от ошибки к успешной загрузке**

1.  Пользователь находится на странице, которая вызвала ошибку. Кастомный экран ошибки виден. `uiState` находится в состоянии `Error`, `pageHasError` равен `true`.
2.  Пользователь переходит на новую, рабочую страницу.
3.  Вызывается `onPageStarted` для новой страницы. Флаг `pageHasError` сбрасывается в `false`. Кастомный экран ошибки **продолжает отображаться**.
4.  Метод `onReceivedError` не вызывается.
5.  Вызывается `onPageCommitVisible` (для API 23+). В этот момент кастомный экран ошибки скрывается, а `uiState` сбрасывается в `Content`. Пользователь видит плавный переход от экрана ошибки к контенту новой страницы.
6.  *(Для API < 23)* Вызывается `onPageFinished`. Он проверяет, что `pageHasError` равен `false`, и только после этого скрывает экран ошибки.

---
@@@@
class CustomWebViewClient(
private val chromeView: ChromeView
) : WebViewClient() {

    private var pageHasError = false

    // -----------------------------
    // Navigation start
    // -----------------------------
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        pageHasError = false
        chromeView.onPageStarted()
        // ❗️ Ошибку НЕ скрываем
    }

    // -----------------------------
    // Errors
    // -----------------------------
    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceError
    ) {
        if (!request.isForMainFrame) return

        pageHasError = true
        chromeView.setErrorPage(
            code = error.errorCode,
            description = error.description?.toString()
        )
    }

    override fun onReceivedHttpError(
        view: WebView,
        request: WebResourceRequest,
        errorResponse: WebResourceResponse
    ) {
        if (!request.isForMainFrame) return

        pageHasError = true
        chromeView.setErrorPage(
            code = errorResponse.statusCode,
            description = errorResponse.reasonPhrase
        )
    }

    // -----------------------------
    // Success (API 23+)
    // -----------------------------
    override fun onPageCommitVisible(view: WebView?, url: String?) {
        if (!pageHasError) {
            chromeView.removeErrorPage()
        }
    }

    // -----------------------------
    // Fallback (API < 23)
    // -----------------------------
    override fun onPageFinished(view: WebView?, url: String?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && !pageHasError) {
            chromeView.removeErrorPage()
        }
        chromeView.onPageFinished()
    }
}
