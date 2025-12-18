package com.karan.hashin.screens.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import com.karan.hashin.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.internal.rememberComposableLambda
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.karan.hashin.navigation.Screens
import com.karan.hashin.viewmodel.SplashViewModel

@Composable
fun Splash(
    viewModel : SplashViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var animate by remember { mutableStateOf(false) }
    val logoPainter =  painterResource(id = R.drawable.vault)

    LaunchedEffect(Unit) {
        animate = true
        viewModel.move {
            navController.navigate(if (viewModel.auth.currentUser != null) Screens.Home.route else Screens.Auth.route) {
                popUpTo(Screens.Splash.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (animate) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1200,
            easing = FastOutSlowInEasing
        ),
        label = "scale animation"
    )

    val alpha by animateFloatAsState(
        targetValue = if (animate) 1f else 0f,
        animationSpec = tween(durationMillis = 1200),
        label = "alpha animation"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = logoPainter,
            contentDescription = "App Logo",
            modifier = Modifier
                .size(144.dp)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    alpha = alpha
                )
        )
    }
}