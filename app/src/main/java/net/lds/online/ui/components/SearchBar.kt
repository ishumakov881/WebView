package net.lds.online.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    isVisible: Boolean,
    onSearch: (String) -> Unit,
    onClose: () -> Unit,
    isDarkTheme: Boolean,
    matchCount: Int = 0,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val density = LocalDensity.current
    var offsetY by remember { mutableStateOf(0f) }
    val swipeThreshold = with(density) { 50.dp.toPx() }

    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            if (offsetY > swipeThreshold) {
                                onClose()
                                searchQuery = ""
                                keyboardController?.hide()
                            }
                            offsetY = 0f
                        },
                        onDragCancel = { offsetY = 0f },
                        onVerticalDrag = { _, dragAmount ->
                            offsetY += dragAmount
                        }
                    )
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { 
                    searchQuery = it
                    if (it.isNotEmpty()) {
                        onSearch(it)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                placeholder = { Text("Поиск...") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                    unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Поиск",
                        tint = if (isDarkTheme) Color.White else Color.Black
                    )
                },
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (searchQuery.isNotEmpty()) {
                            Text(
                                text = "$matchCount",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            IconButton(
                                onClick = {
                                    searchQuery = ""
                                    keyboardController?.hide()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Очистить",
                                    tint = if (isDarkTheme) Color.White else Color.Black
                                )
                            }
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch(searchQuery)
                        keyboardController?.hide()
                    }
                )
            )
        }
    }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            focusRequester.requestFocus()
        }
    }
} 