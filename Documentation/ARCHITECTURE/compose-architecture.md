# 🏗️ Архитектурные правила Jetpack Compose

1. **Никогда** не передавай ViewModel напрямую в компоненты UI
2. **Всегда** разделяй UI и логику на отдельные компоненты
3. **Сначала** создавай UI компоненты, которые принимают только данные и колбэки
4. **Затем** оборачивай их в контейнеры, которые работают с ViewModel
5. **Важно** не прокидывать в @Composable скрины навигатор! только колбек, а уже на уровне
   навигатора - навигация

- ✅ **Android Studio не отображает @Preview** для компонентов с koinViewModel()
- ✅ **Тестирование становится проще** с мок-данными
- ✅ **Переиспользуемость** UI компонентов возрастает
- ✅ **Код становится чище** и более модульным

```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    
    Column {
        Text("Заголовок")
        
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(state.items) { item ->
                    ItemView(
                        item = item,
                        onClick = { viewModel.selectItem(it) }
                    )
                }
            }
        }
        
        Button(onClick = { viewModel.loadData() }) {
            Text("Обновить")
        }
    }
    
    // Невозможно создать Preview для этого компонента!
}
```

```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    
    // Передаем только данные и колбэки
    MyScreenBody(
        state = state,
        onItemClick = { viewModel.selectItem(it) },
        onRefreshClick = { viewModel.loadData() }
    )
}
```

```kotlin
@Composable
fun MyScreenBody(
    state: MyState,
    onItemClick: (Item) -> Unit,
    onRefreshClick: () -> Unit
) {
    Column {
        Text("Заголовок")
        
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(state.items) { item ->
                    ItemView(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
        
        Button(onClick = onRefreshClick) {
            Text("Обновить")
        }
    }
}
```

```kotlin
@Preview(showBackground = true)
@Composable
fun MyScreenPreview() {
    val mockItems = listOf(
        Item(1, "Первый элемент"),
        Item(2, "Второй элемент"),
        Item(3, "Третий элемент")
    )
    
    val mockState = MyState(
        items = mockItems,
        isLoading = false
    )
    
    // Теперь можно легко предварительно просмотреть компонент!
    MyScreenBody(
        state = mockState,
        onItemClick = {},  // Пустые колбэки для Preview
        onRefreshClick = {}
    )
}

// Дополнительный Preview для состояния загрузки
@Preview(showBackground = true)
@Composable
fun MyScreenLoadingPreview() {
    MyScreenBody(
        state = MyState(isLoading = true),
        onItemClick = {},
        onRefreshClick = {}
    )
}
```

1. **Легкое тестирование** - создавай сколько угодно @Preview с разными состояниями
2. **Изолированные UI-компоненты** - UI не зависит от бизнес-логики
3. **Лучшая переиспользуемость** - UI компоненты можно повторно использовать
4. **Чистая архитектура** - четкое разделение ответственности
5. **Удобство разработки** - возможность итеративно развивать UI без перезапуска приложения

Всегда пиши весь код, включая ViewModel, все функции и классы в одном файле для каждого экрана. Это
упрощает навигацию по коду и делает его более понятным.

```
YourScreen/
└── YourScreenFile.kt  // Содержит ViewModel, State, Screen и Body
```

```kotlin
// 1. Data models and state
data class MyState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class Item(val id: Int, val name: String)

// 2. ViewModel
class MyViewModel : ViewModel() {
    private val _state = MutableStateFlow(MyState())
    val state: StateFlow<MyState> = _state
    
    fun loadData() { /* implementation */ }
    fun selectItem(item: Item) { /* implementation */ }
}

// 3. Container component
@Composable
fun MyScreen(viewModel: MyViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    
    MyScreenBody(
        state = state,
        onItemClick = { viewModel.selectItem(it) },
        onRefreshClick = { viewModel.loadData() }
    )
}

// 4. UI component
@Composable
fun MyScreenBody(
    state: MyState,
    onItemClick: (Item) -> Unit,
    onRefreshClick: () -> Unit
) {
    // UI implementation
}

// 5. Previews
@Preview
@Composable
fun MyScreenPreview() {
    // Preview implementation with mock data
}
``` 