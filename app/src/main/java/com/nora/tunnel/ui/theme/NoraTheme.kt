package com.nora.tunnel.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    primary = Color(0xFF7C4DFF),
    surface = Color(0xFF0F1115),
    surfaceVariant = Color(0xFF1A1D24),
    background = Color(0xFF0A0C10),
    error = Color(0xFFFF5449)
)

@Composable
fun NoraTheme(content: @Composable ()->Unit) {
    MaterialTheme(colorScheme = DarkScheme, typography = Typography(), content = content)
}
