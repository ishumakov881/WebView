package com.knopka.kz.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.knopka.kz.R

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var page by remember { mutableStateOf(0) }
    val titles = listOf(
        stringResource(id = R.string.onboarding_title_1),
        stringResource(id = R.string.onboarding_title_2)
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_logo_round),
                contentDescription = null,
                modifier = Modifier.size(220.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                textAlign = TextAlign.Center,
                text = titles[page],
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 22.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(onClick = {
                if (page == titles.lastIndex) {
                    onFinish()
                } else {
                    page++
                }
            }) {
                Text(text = stringResource(id = R.string.onboarding_continue))
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(titles.size) { i ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(12.dp)
                            .then(
                                if (i == page) Modifier else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (i == page) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                            modifier = Modifier.size(8.dp)
                        ) {}
                    }
                }
            }
        }
    }
} 