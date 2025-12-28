package com.example.controlejardim.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Amber400,
    onPrimary = NeutralDark,
    primaryContainer = Amber800,
    onPrimaryContainer = Amber100,
    secondary = Orange500,
    onSecondary = Color.White,
    secondaryContainer = Orange700,
    onSecondaryContainer = Amber100,
    tertiary = DeepOrange500,
    onTertiary = Color.White,
    background = CreamDark,
    onBackground = Amber100,
    surface = SurfaceDark,
    onSurface = Amber100,
    surfaceVariant = Color(0xFF3D3530),
    onSurfaceVariant = Amber200,
    outline = Amber700,
    outlineVariant = Color(0xFF4A3F38)
)

private val LightColorScheme = lightColorScheme(
    primary = Amber700,
    onPrimary = Color.White,
    primaryContainer = Amber100,
    onPrimaryContainer = Amber900,
    secondary = Orange500,
    onSecondary = Color.White,
    secondaryContainer = Amber200,
    onSecondaryContainer = Orange700,
    tertiary = DeepOrange500,
    onTertiary = Color.White,
    background = Cream,
    onBackground = NeutralDark,
    surface = SurfaceLight,
    onSurface = NeutralDark,
    surfaceVariant = Amber50,
    onSurfaceVariant = Amber900,
    outline = Amber300,
    outlineVariant = Amber100
)

@Composable
fun ControleJardimTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

