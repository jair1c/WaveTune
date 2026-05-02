package com.wavetune.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─── Colors ──────────────────────────────────────────────────────────────────

val BackgroundLight = Color(0xFFF5F4F0)
val BackgroundDark = Color(0xFF0F0F0F)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF1A1A1A)
val OnSurfaceLight = Color(0xFF0D0D0D)
val OnSurfaceDark = Color(0xFFE8E8E0)
val AccentBlack = Color(0xFF0D0D0D)
val AccentDark = Color(0xFFE8E8E0)
val SubtleLight = Color(0xFF8A8A82)
val SubtleDark = Color(0xFF606058)
val DividerLight = Color(0xFFE0DED8)
val DividerDark = Color(0xFF2A2A2A)

private val LightColors = lightColorScheme(
    primary = AccentBlack,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8E8E0),
    onPrimaryContainer = AccentBlack,
    secondary = SubtleLight,
    onSecondary = Color.White,
    background = BackgroundLight,
    onBackground = OnSurfaceLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = Color(0xFFECEBE6),
    onSurfaceVariant = SubtleLight,
    outline = DividerLight
)

private val DarkColors = darkColorScheme(
    primary = AccentDark,
    onPrimary = AccentBlack,
    primaryContainer = Color(0xFF2A2A2A),
    onPrimaryContainer = AccentDark,
    secondary = SubtleDark,
    onSecondary = AccentBlack,
    background = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = Color(0xFF222222),
    onSurfaceVariant = SubtleDark,
    outline = DividerDark
)

// ─── Typography ───────────────────────────────────────────────────────────────

// Using system fonts to avoid external dependencies; in production, embed custom fonts
val WaveTuneTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 48.sp,
        lineHeight = 52.sp,
        letterSpacing = (-1.5).sp
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 40.sp,
        letterSpacing = (-1).sp
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.3).sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.2).sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.3.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )
)

// ─── Theme Composable ─────────────────────────────────────────────────────────

@Composable
fun WaveTuneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = WaveTuneTypography,
        content = content
    )
}
