package com.itirafapp.android.presentation.main

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.util.Consumer
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itirafapp.android.presentation.navigation.RootNavigation
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.util.constant.ThemeConfig

@Composable
fun ItirafApp(
    viewModel: MainViewModel = hiltViewModel()
) {
    val currentThemeConfig by viewModel.themeState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    LaunchedEffect(Unit) {
        activity?.intent?.let { intent ->
            viewModel.handleNotificationIntent(intent)
            activity.intent = null
        }
    }

    DisposableEffect(Unit) {
        val listener = Consumer<Intent> { intent ->
            viewModel.handleNotificationIntent(intent)
        }
        activity?.addOnNewIntentListener(listener)
        onDispose {
            activity?.removeOnNewIntentListener(listener)
        }
    }

    val isDarkTheme = when (currentThemeConfig) {
        ThemeConfig.LIGHT -> false
        ThemeConfig.DARK -> true
        ThemeConfig.SYSTEM -> isSystemInDarkTheme()
    }

    ItirafAppTheme(darkTheme = isDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RootNavigation(mainViewModel = viewModel)
        }
    }
}