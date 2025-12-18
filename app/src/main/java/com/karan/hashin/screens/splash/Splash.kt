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
import com.karan.hashin.LocalBiometricAuth

@Composable
fun Splash(
    viewModel : SplashViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var animate by remember { mutableStateOf(false) }
    val logoPainter =  painterResource(id = R.drawable.vault)
    val biometric = LocalBiometricAuth.current

    LaunchedEffect(Unit) {
        animate = true
        viewModel.move {
            if (viewModel.auth.currentUser != null) {
                val auth = biometric
                if (auth != null && com.karan.hashin.utils.BiometricAuth.isAvailable(context = navController.context)) {
                    auth.authenticate(
                        title = "Unlock Hashin",
                        subtitle = "Confirm your identity"
                    ,
                        callback = object : com.karan.hashin.utils.BiometricAuth.Callback {
                            override fun onSuccess() {
                                navController.navigate(Screens.Home.route) {
                                    popUpTo(Screens.Splash.route) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                            override fun onFailure(error: String) {
                                navController.navigate(Screens.Auth.route) {
                                    popUpTo(Screens.Splash.route) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                } else {
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            } else {
                navController.navigate(Screens.Auth.route) {
                    popUpTo(Screens.Splash.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (animate) 1f else 0f,
        animationSpec = tween(
            durationMillis = 900,
            easing = FastOutSlowInEasing
        ),
        label = "scale animation"
    )

    val alpha by animateFloatAsState(
        targetValue = if (animate) 1f else 0f,
        animationSpec = tween(durationMillis = 900),
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