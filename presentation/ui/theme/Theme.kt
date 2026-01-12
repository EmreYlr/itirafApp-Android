package com.itirafapp.android.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect

private val DarkColorScheme = darkColorScheme(
    primary = DarkBrandPrimary,
    secondary = DarkBrandSecondary,
    tertiary = DarkActionLike,
    background = DarkBackgroundApp,
    surface = DarkBackgroundCard,
    error = DarkStatusError,
    onPrimary = Color.White,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    outline = DarkDividerColor,
    surfaceVariant = DarkTextTertiary
)

private val LightColorScheme = lightColorScheme(
    primary = LightBrandPrimary,
    secondary = LightBrandSecondary,
    tertiary = LightActionLike,
    background = LightBackgroundApp,
    surface = LightBackgroundCard,
    error = LightStatusError,
    onPrimary = Color.White,
    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary,
    outline = LightDividerColor,
    surfaceVariant = LightTextTertiary
)
@Immutable
data class ItirafColors(
    val actionLike: Color,
    val backgroundApp: Color,
    val backgroundCard: Color,
    val brandPrimary: Color,
    val brandSecondary: Color,
    val dividerColor: Color,
    val sensitiveAccent: Color,
    val statusError: Color,
    val statusPending: Color,
    val statusSuccess: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color
)

val LightItirafColors = ItirafColors(
    actionLike = Color(0xFFFF4D4D),
    backgroundApp = Color(0xFFF8F9FD),
    backgroundCard = Color(0xFFFFFFFF),
    brandPrimary = Color(0xFF5B4CD4),
    brandSecondary = Color(0xFF7C6FE4),
    dividerColor = Color(0xFFE2E7EA),
    sensitiveAccent = Color(0xFFD63031),
    statusError = Color(0xFFFF4757),
    statusPending = Color(0xFFFFA000),
    statusSuccess = Color(0xFF0D9448),
    textPrimary = Color(0xFF2D3436),
    textSecondary = Color(0xFF636E72),
    textTertiary = Color(0xFF4B5563)
)

val DarkItirafColors = ItirafColors(
    actionLike = Color(0xFFFF6B6B),
    backgroundApp = Color(0xFF13131A),
    backgroundCard = Color(0xFF1E1E2A),
    brandPrimary = Color(0xFF8A7DF0),
    brandSecondary = Color(0xFF5F27CD),
    dividerColor = Color(0xFF2F3542),
    sensitiveAccent = Color(0xFFFF6B6B),
    statusError = Color(0xFFFF4757),
    statusPending = Color(0xFFFFCA28),
    statusSuccess = Color(0xFF2ED573),
    textPrimary = Color(0xFFF1F2F6),
    textSecondary = Color(0xFFA4B0BE),
    textTertiary = Color(0xFF9CA3AF)
)

val LocalItirafColors = staticCompositionLocalOf { LightItirafColors }

@Composable
fun ItirafAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkItirafColors else LightItirafColors

    val materialScheme = if (darkTheme) {
        darkColorScheme(
            primary = colors.brandPrimary,
            secondary = colors.brandSecondary,
            background = colors.backgroundApp,
            surface = colors.backgroundCard,
            error = colors.statusError,
            onBackground = colors.textPrimary,
            onSurface = colors.textPrimary
        )
    } else {
        lightColorScheme(
            primary = colors.brandPrimary,
            secondary = colors.brandSecondary,
            background = colors.backgroundApp,
            surface = colors.backgroundCard,
            error = colors.statusError,
            onBackground = colors.textPrimary,
            onSurface = colors.textPrimary
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.backgroundApp.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalItirafColors provides colors) {
        MaterialTheme(
            colorScheme = materialScheme,
            typography = Typography,
            content = content
        )
    }
}

object ItirafTheme {
    val colors: ItirafColors
        @Composable
        get() = LocalItirafColors.current
}