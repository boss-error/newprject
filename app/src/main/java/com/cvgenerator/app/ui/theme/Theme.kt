package com.cvgenerator.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Modern gradient colors
val PrimaryBlue = Color(0xFF6366F1) // Indigo-500
val PrimaryBlueDark = Color(0xFF4F46E5) // Indigo-600
val SecondaryPurple = Color(0xFF8B5CF6) // Violet-500
val AccentTeal = Color(0xFF06B6D4) // Cyan-500
val AccentGreen = Color(0xFF10B981) // Emerald-500
val NeutralGray = Color(0xFF6B7280) // Gray-500
val LightGray = Color(0xFFF9FAFB) // Gray-50
val DarkGray = Color(0xFF111827) // Gray-900
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF1F2937) // Gray-800

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    primaryContainer = PrimaryBlueDark,
    secondary = SecondaryPurple,
    secondaryContainer = Color(0xFF7C3AED), // Violet-600
    tertiary = AccentTeal,
    tertiaryContainer = Color(0xFF0891B2), // Cyan-600
    background = DarkGray,
    surface = SurfaceDark,
    surfaceVariant = Color(0xFF374151), // Gray-700
    onPrimary = Color.White,
    onPrimaryContainer = Color.White,
    onSecondary = Color.White,
    onSecondaryContainer = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFFF3F4F6), // Gray-100
    onSurface = Color(0xFFF3F4F6), // Gray-100
    onSurfaceVariant = Color(0xFFD1D5DB), // Gray-300
    outline = Color(0xFF9CA3AF), // Gray-400
    error = Color(0xFFEF4444), // Red-500
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    primaryContainer = Color(0xFFEEF2FF), // Indigo-50
    secondary = SecondaryPurple,
    secondaryContainer = Color(0xFFF5F3FF), // Violet-50
    tertiary = AccentTeal,
    tertiaryContainer = Color(0xFFECFDF5), // Emerald-50
    background = LightGray,
    surface = SurfaceLight,
    surfaceVariant = Color(0xFFF3F4F6), // Gray-100
    onPrimary = Color.White,
    onPrimaryContainer = PrimaryBlueDark,
    onSecondary = Color.White,
    onSecondaryContainer = Color(0xFF7C3AED), // Violet-600
    onTertiary = Color.White,
    onBackground = DarkGray,
    onSurface = DarkGray,
    onSurfaceVariant = NeutralGray,
    outline = Color(0xFFD1D5DB), // Gray-300
    error = Color(0xFFEF4444), // Red-500
    onError = Color.White
)

@Composable
fun CVGeneratorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
