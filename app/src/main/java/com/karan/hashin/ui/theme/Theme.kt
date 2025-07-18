package com.karan.hashin.ui.theme

import android.graphics.Color.TRANSPARENT
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(

    // Background
    background = Color(0xFF121212),

    // Primary Scheme
    primary = Color.White,   // Icon color
    primaryContainer = Color(0xFF292929),
//    onPrimaryContainer = Color(0xFFEADDFF),

    // Secondary Scheme
    secondary = Color(0xFF9C27B0),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF41484D),
    onSecondaryContainer = Color.White,

    // Tertiary Scheme
    tertiary = Color(0xFFF0F0F0),
    onTertiary = Color(0xFF666666),
    tertiaryContainer = Color.Black,
    onTertiaryContainer = Color.White,

    outline = BlueSelectionDark,

    // Surface
    surface = SurfaceDark,
    surfaceTint = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    surfaceContainer = Color(0xFF242424),
    onSurface = Color.White,
    onSurfaceVariant = Color.White,
//    surfaceContainerLow = Color(0xFF1B1B1B),
//    surfaceContainerHigh = Color(0xFF2E2E2E)
)

private val LightColorScheme = lightColorScheme(

    // BackGround
    background = Color(0xFFFFFBFE),

    // Primary Scheme
    primary = Color.Black,
    primaryContainer = Color(0xFFEADDFF),
//    onPrimaryContainer = Color(0xFF21005D),

    // Secondary Scheme
    secondary = Color(0xFF9C27B0),
    onSecondary = Color.White,
    secondaryContainer = Color.White,

    // Tertiary Scheme
    tertiary = Color(0xFFF0F0F0),
    onTertiary = Color(0xFF666666),
    tertiaryContainer = Color.White,

    outline = BlueSelectionLight,

    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    surfaceContainer = Color(0xFFF7F7F7),
    surfaceTint = SurfaceLight,
    onSurface = Color.Black,
    onSurfaceVariant = Color.Black,

)

object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified
    @Composable
    override fun rippleAlpha() = RippleAlpha(0f, 0f, 0f, 0f)
}

@Composable
fun HashinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val activity = LocalActivity.current as ComponentActivity
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }


    DisposableEffect(darkTheme) {
        val barStyle = if (darkTheme) {
            SystemBarStyle.dark(
                scrim = TRANSPARENT
            )
        } else {
            SystemBarStyle.light(
                scrim = TRANSPARENT,
                darkScrim = TRANSPARENT
            )
        }
        activity.enableEdgeToEdge(
            statusBarStyle = barStyle,
            navigationBarStyle = barStyle
        )
        onDispose { }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,

        ) {
        CompositionLocalProvider(
            LocalRippleTheme provides NoRippleTheme,
        ) {
            content()
        }
    }
}