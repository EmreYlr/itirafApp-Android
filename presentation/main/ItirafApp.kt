package com.itirafapp.android.presentation.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.presentation.navigation.RootNavigation
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.util.constant.ThemeConfig

@Composable
fun ItirafApp(
    viewModel: MainViewModel = hiltViewModel()
) {
    val currentThemeConfig by viewModel.themeState.collectAsState()

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
            RootNavigation()
        }
    }
}