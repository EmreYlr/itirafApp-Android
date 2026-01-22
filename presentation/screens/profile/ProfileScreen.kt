package com.itirafapp.android.presentation.screens.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ProfileUiEvent.NavigateToSettings -> {
                    onNavigateToSettings()
                }
            }
        }
    }

    ProfileContent(
        state = state,
        onSettingsClick = {
            focusManager.clearFocus()
            viewModel.onEvent(ProfileEvent.SettingsClicked)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    state: ProfileState,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.profile_title),
                actions = {
                    IconButton(
                        onClick = onSettingsClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu Icon",
                            tint = ItirafTheme.colors.textSecondary,
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

    }
}