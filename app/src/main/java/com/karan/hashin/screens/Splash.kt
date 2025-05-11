package com.karan.hashin.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import com.karan.hashin.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.karan.hashin.viewmodel.SplashViewModel

@Composable
fun Splash(
    viewModel : SplashViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var animate by remember { mutableStateOf(false) }
    viewModel.move {
        navController.navigate(viewModel.to)
    }

    val scale by animateFloatAsState(
        targetValue = if(animate) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1500,
            easing = FastOutSlowInEasing
        ),
        label = "scale animation"
    )

    val alpha by animateFloatAsState(
        targetValue = if(animate) 1f else 0f,
        animationSpec = tween(durationMillis = 2000),
        label = "alpha animation"
    )

    LaunchedEffect(key1 = true) {
        animate = true
//        viewModel.move {
//            navController.navigate(viewModel.to)
//        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.vault),
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