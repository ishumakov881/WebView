Скрины:
Скрины не принимают навигатор, а колбеком отстукивают наверх.
Не писать все состояния скрина единым блоком кода, а разбивать на функции
Пример:

- Неправильно
  // Отображаем состояние загрузки
  if (uiState.isLoading) {
  Box(
  modifier = Modifier.fillMaxSize(),
  contentAlignment = Alignment.Center
  ) {
  CircularProgressIndicator()
  }
  }
- Правильно
  // Отображаем состояние загрузки
  if (uiState.isLoading) {
  ContentLoadingIndicator()
  }

Это позволит каждое состояние тестировать, и под каждое делать Preview если необходимо

1. пиши все полученные функции в классе скрина