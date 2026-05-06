package com.dma.studentapplication.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary          = Green500,
    onPrimary        = Color.White,
    primaryContainer = Green100,
    secondary        = Green600,
    background       = Background,
    surface          = Surface,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary,
    outline          = Border,
    error            = WrongRed,
)

private val DarkColors = darkColorScheme(
    primary          = Green500,
    onPrimary        = Color.White,
    primaryContainer = Color(0xFF14532D),
    secondary        = Green600,
    background       = DarkBackground,
    surface          = DarkSurface,
    onBackground     = Color(0xFFF1F5F9),
    onSurface        = Color(0xFFF1F5F9),
    outline          = DarkBorder,
    error            = WrongRed,
)

@Composable
fun StudentApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}