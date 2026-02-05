package com.itirafapp.android.presentation.screens.profile.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.core.ItirafButton
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.profile.components.LanguageSelectionContent
import com.itirafapp.android.presentation.screens.profile.components.SettingsRow
import com.itirafapp.android.presentation.screens.profile.components.ThemeSelectionContent
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.extension.openUrlSafe
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingsScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToEdit: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val colorParams = ItirafTheme.colors.brandPrimary.toArgb()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is SettingsUiEvent.NavigateToLogin -> {
                    onNavigateToLogin()
                }

                is SettingsUiEvent.NavigateToUrl -> {
                    openUrlSafe(context, event.url, colorParams)
                }

                is SettingsUiEvent.NavigateToEdit -> {
                    onNavigateToEdit()
                }

                is SettingsUiEvent.NavigateToNotification -> {
                    onNavigateToNotification()
                }

                is SettingsUiEvent.NavigateToBack -> {
                    onBackClick()
                }

                is SettingsUiEvent.CopyToClipboard -> {
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Mail Adresi", event.text)
                    clipboard.setPrimaryClip(clip)

                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }

                is SettingsUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    SettingsContent(
        state = state,
        onEvent = viewModel::onEvent,
        onLogoutClick = {
            focusManager.clearFocus()
            viewModel.onEvent(SettingsEvent.LogoutClicked)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    onLogoutClick: () -> Unit
) {
    if (state.showThemeDialog) {
        Dialog(onDismissRequest = { onEvent(SettingsEvent.DismissThemeDialog) }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                ThemeSelectionContent(
                    currentTheme = state.currentTheme,
                    onThemeSelected = { newTheme ->
                        onEvent(SettingsEvent.ThemeSelected(newTheme))
                    },
                    onDismiss = {
                        onEvent(SettingsEvent.DismissThemeDialog)
                    }
                )
            }
        }
    }

    if (state.showLanguageDialog) {
        Dialog(onDismissRequest = { onEvent(SettingsEvent.DismissLanguageDialog) }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                LanguageSelectionContent(
                    currentLanguage = state.currentLanguage,
                    onLanguageSelected = { selectedConfig ->
                        onEvent(SettingsEvent.LanguageSelected(selectedConfig))
                    }
                )
            }
        }
    }

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.settings_title),
                canNavigateBack = true,
                onNavigateBack = { onEvent(SettingsEvent.OnBackClicked) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            state.sections.forEach { sectionModel ->

                Text(
                    text = stringResource(id = sectionModel.section.titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    color = ItirafTheme.colors.textSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ItirafTheme.colors.backgroundCard
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column {
                        sectionModel.items.forEachIndexed { index, item ->

                            SettingsRow(
                                item = item,
                                onClick = { onEvent(SettingsEvent.ItemClicked(item.type)) }
                            )

                            if (index < sectionModel.items.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    thickness = 0.5.dp,
                                    color = ItirafTheme.colors.dividerColor
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            val buttonText =
                if (state.isAnonymous) stringResource(R.string.login_button)
                else stringResource(R.string.logout_button)

            val baseColor =
                if (state.isAnonymous) ItirafTheme.colors.brandPrimary
                else ItirafTheme.colors.statusError

            val buttonIcon =
                if (state.isAnonymous) Icons.AutoMirrored.Filled.Login
                else Icons.AutoMirrored.Filled.Logout

            ItirafButton(
                text = buttonText,
                containerColor = baseColor.copy(alpha = 0.1f),
                contentColor = baseColor,
                borderColor = baseColor,
                borderWidth = 1.dp,
                onClick = onLogoutClick,
                isLoading = state.isLoading,
                icon = buttonIcon,
                iconTint = baseColor
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}