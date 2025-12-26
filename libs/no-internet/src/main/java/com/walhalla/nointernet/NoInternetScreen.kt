package com.walhalla.nointernet

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AirplanemodeInactive
import androidx.compose.material.icons.rounded.SwapVert
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.imaginativeworld.oopsnointernet.utils.NoInternetUtils


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoInternetScreen(onDismiss: ()-> Unit) {
    val context = LocalContext.current
    //NoInternetPendulumAnimation()

    // Dialog function
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false // experimental
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {

           Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {


                NoInternetSignalAnimation(
                    modifier = Modifier,//.border(2.dp, Color.Red),
                    isAirplaneModeOn = true
                )

//                Image(
//                    painter = painterResource(id = R.drawable.ic_no_internet_no_bg),
//                    //painter = painterResource(id = R.drawable.ic_no_wifi_2),
//
//
//                    contentDescription = null,
//                    contentScale = ContentScale.Fit,
//                    modifier = Modifier
//                        .height(200.dp)
//                        .fillMaxWidth(),
//
//                    )

                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    //.........................Text: title
                    Text(
                        text = stringResource(R.string.default_title),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    //.........................Text : description
                    Text(
                        text = stringResource(R.string.default_message),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                            .fillMaxWidth(),
                        letterSpacing = 1.sp,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    //.........................Spacer
                    Spacer(modifier = Modifier.height(24.dp))

                    val cornerRadius = 16.dp
                    val gradientColor = listOf(Color(0xFFff669f), Color(0xFFff8961))

//                    GradientButton(
//                        gradientColors = gradientColor,
//                        cornerRadius = cornerRadius,
//                        nameButton = "Try again",
//                        roundedCornerShape = RoundedCornerShape(topStart = 30.dp,bottomEnd = 30.dp),
//                        onClick = onDismiss
//                    )

                    InternetConnectionActions(
                        modifier = Modifier,
                        isAirplaneModeOn = false,
                        showInternetOnButtons = true,
                        showAirplaneModeOffButtons = true,
                        onWifiClick = {
                            NoInternetUtils.turnOnWifi(context)
                        },
                        onMobileDataClick = {
                            NoInternetUtils.turnOnMobileData(context)
                        },
                        onAirplaneOffClick = {}
                    )
                }

            }

        }
    }
}
@Composable
fun InternetConnectionActions(
    modifier: Modifier = Modifier,
    isAirplaneModeOn: Boolean,
    showInternetOnButtons: Boolean = true,
    showAirplaneModeOffButtons: Boolean = true,
    onWifiClick: () -> Unit,
    onMobileDataClick: () -> Unit,
    onAirplaneOffClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),                                                                    
    horizontalAlignment = Alignment.CenterHorizontally                                                     
    ) {
        if (isAirplaneModeOn) {
            // Раздел "Please turn off Airplane Mode"апрос выполненным.
            if (showAirplaneModeOffButtons) {
                Text(
                    text = stringResource(R.string.please_turn_off),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 24.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textTransform = TextTransform.Uppercase // Для заглавных букв
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onAirplaneOffClick,
                    shape = RoundedCornerShape(100.dp), // Закругленные углы
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AirplanemodeInactive, // Используем Compose Material Icons
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = stringResource(R.string.airplane_mode),
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            // Раздел "Please turn on Internet"
            if (showInternetOnButtons) {
                Text(
                    text = stringResource(R.string.please_turn_on),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 24.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textTransform = TextTransform.Uppercase // Для заглавных букв
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center, // Соответствует "packed" chain style
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onWifiClick,
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Wifi,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            text = stringResource(R.string.wifi),
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp)) // padding 4dp между кнопками
                    Button(
                        onClick = onMobileDataClick,
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SwapVert,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            text = stringResource(R.string.mobile_data),
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp)) // padding bottom 24dp
            }
        }
    }
}

// Вспомогательный класс для TextTransform, так как в Compose нет прямого аналога allCaps
enum class TextTransform { Uppercase, None }

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    textTransform: TextTransform = TextTransform.None
) {
    val transformedText = if (textTransform == TextTransform.Uppercase) text.uppercase() else text
    Text(
        text = transformedText,
        modifier = modifier,
        textAlign = textAlign,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight
    )
}
@Composable
fun NoInternetPendulumAnimation(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pendulum")

    // Анимация вращения
    val rotation by infiniteTransition.animateValue(
        initialValue = -15f,
        targetValue = 15f,
        typeConverter = Float.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    // Анимация перемещения
    val translationX by infiniteTransition.animateValue(
        initialValue = (-16).dp,
        targetValue = 16.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "translation"
    )

    Box(
        modifier = modifier.size(128.dp),
        contentAlignment = Alignment.Center
    ) {
        // Иконка ic_no_wifi_2, которая вращается (соответствует no_internet_img_1)
        Image(
            painter = painterResource(id = R.drawable.ic_no_wifi_2),
            contentDescription = null,
            modifier = Modifier.graphicsLayer {
                this.rotationZ = rotation
            }
        )
        // Иконка ic_no_wifi_1, которая перемещается (соответствует no_internet_img_2)
        Image(
            painter = painterResource(id = R.drawable.ic_no_wifi_1),
            contentDescription = null,
            modifier = Modifier.graphicsLayer {
                this.translationX = translationX.toPx()
            }
        )
    }
}

//...........................................................................
@Composable
fun GradientButton(
    gradientColors: List<Color>,
    cornerRadius: Dp,
    nameButton: String,
    roundedCornerShape: RoundedCornerShape,
    onClick: ()-> Unit
) {

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp),
        onClick = onClick,

        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(cornerRadius)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(colors = gradientColors),
                    shape = roundedCornerShape
                )
                .clip(roundedCornerShape)
                /*.background(
                    brush = Brush.linearGradient(colors = gradientColors),
                    shape = RoundedCornerShape(cornerRadius)
                )*/
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = nameButton,
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
private fun PreviewInternetConnectionActions() {
    InternetConnectionActions(
        modifier = Modifier,
        isAirplaneModeOn = false,
        showInternetOnButtons = true,
        showAirplaneModeOffButtons = true,
        onWifiClick = {},
        onMobileDataClick = {},
        onAirplaneOffClick = {}
    )
}
