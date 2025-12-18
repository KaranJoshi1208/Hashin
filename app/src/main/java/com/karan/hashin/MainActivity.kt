package com.karan.hashin

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.karan.hashin.navigation.NavGraph
import com.karan.hashin.ui.theme.HashinTheme
import com.karan.hashin.ui.theme.LocalDarkTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val darkThemeState = remember { mutableStateOf(getDarkThemePreference(this)) }
            CompositionLocalProvider(LocalDarkTheme provides darkThemeState) {
                HashinTheme {
                    navController = rememberNavController()
                    NavGraph(navController)
                }
            }

            // Update status bar icon color based on theme
            LaunchedEffect(darkThemeState.value) {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !darkThemeState.value
                }
            }
        }
    }
}

private fun getDarkThemePreference(context: Context): Boolean {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return prefs.getBoolean("dark_theme", false)
}

private fun setDarkThemePreference(context: Context, isDark: Boolean) {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putBoolean("dark_theme", isDark).apply()
}
