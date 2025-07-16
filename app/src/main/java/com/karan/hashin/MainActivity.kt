package com.karan.hashin

import android.app.Activity
import android.graphics.Color.TRANSPARENT
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.karan.hashin.navigation.NavGraph
import com.karan.hashin.ui.theme.HashinTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val lightTransparentStyle = SystemBarStyle.light(
            scrim = TRANSPARENT,
            darkScrim = TRANSPARENT
        )

        enableEdgeToEdge(
            statusBarStyle = lightTransparentStyle,
            navigationBarStyle = lightTransparentStyle
        )

        setContent {
            HashinTheme(dynamicColor = false) {
                navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}