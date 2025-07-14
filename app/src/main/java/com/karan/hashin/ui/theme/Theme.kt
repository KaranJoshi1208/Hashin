package com.karan.hashin.ui.theme

import android.app.Activity
import android.os.Build
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(

    // Primary Scheme
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF004A77),
    onPrimaryContainer = Color.White,

    // Secondary Scheme
    secondary = Color(0xFF9C27B0),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF41484D) ,
    onSecondaryContainer = Color.White,

    // Tertiary Scheme
    tertiary = Color(0xFFF0F0F0),
    onTertiary = Color(0xFF666666),
    tertiaryContainer = Color(0xFF4A4458),
    onTertiaryContainer = Color.White,
)

private val LightColorScheme = lightColorScheme(

    // Primary Scheme
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    primaryContainer = Color.White,
    onPrimaryContainer = Color.Black,

    // Secondary Scheme
    secondary = Color(0xFF9C27B0),
    onSecondary = Color.White,
    secondaryContainer = Color.White,

    // Tertiary Scheme
    tertiary = Color(0xFFF0F0F0),
    onTertiary = Color(0xFF666666),

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

object NoRippleTheme : RippleTheme {
    @Composable override fun defaultColor() = Color.Unspecified
    @Composable override fun rippleAlpha() = RippleAlpha(0f, 0f, 0f, 0f)
}

@Composable
fun HashinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
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