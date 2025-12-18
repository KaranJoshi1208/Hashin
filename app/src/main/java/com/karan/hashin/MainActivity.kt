package com.karan.hashin

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.karan.hashin.navigation.NavGraph
import com.karan.hashin.ui.theme.HashinTheme
import com.karan.hashin.ui.theme.LocalDarkTheme
import com.karan.hashin.utils.BiometricAuth

val LocalBiometricAuth = staticCompositionLocalOf<BiometricAuth?> { null }

class MainActivity : FragmentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val darkThemeState = remember { mutableStateOf(getDarkThemePreference(this)) }
            val biometricAuth = remember { BiometricAuth(this) }
            CompositionLocalProvider(
                LocalDarkTheme provides darkThemeState,
                LocalBiometricAuth provides biometricAuth
            ) {
                HashinTheme {
                    navController = rememberNavController()
                    NavGraph(navController)
                }

                LaunchedEffect(darkThemeState.value) {
                    val controller = WindowCompat.getInsetsController(window, window.decorView)
                    controller.isAppearanceLightStatusBars = !darkThemeState.value
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        controller.isAppearanceLightNavigationBars = !darkThemeState.value
                    }
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
