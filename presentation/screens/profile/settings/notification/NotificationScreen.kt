package com.itirafapp.android.presentation.screens.profile.settings.notification

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.profile.components.NotificationRow
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NotificationScreen(
    onBackClick: () -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    BackHandler {
        viewModel.onEvent(NotificationEvent.OnBackClicked)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is NotificationUiEvent.NavigateToBack -> {
                    onBackClick()
                }

                is NotificationUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    NotificationContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = {
            viewModel.onEvent(NotificationEvent.OnBackClicked)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationContent(
    state: NotificationState,
    onEvent: (NotificationEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val switchColors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = ItirafTheme.colors.brandPrimary,
        uncheckedThumbColor = Color.White,
        uncheckedTrackColor = ItirafTheme.colors.dividerColor,
        uncheckedBorderColor = Color.Transparent,
        disabledCheckedTrackColor = ItirafTheme.colors.brandPrimary.copy(alpha = 0.4f),
        disabledUncheckedTrackColor = ItirafTheme.colors.dividerColor.copy(alpha = 0.4f),
        disabledCheckedThumbColor = Color.White.copy(alpha = 0.8f),
        disabledUncheckedThumbColor = Color.White.copy(alpha = 0.8f)
    )

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.notifications),
                canNavigateBack = true,
                onNavigateBack = { onBackClick() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.notification_all_title),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ItirafTheme.colors.textPrimary
                )

                Switch(
                    checked = state.isMasterEnabled,
                    onCheckedChange = { isChecked ->
                        onEvent(NotificationEvent.OnMasterSwitchChanged(isChecked))
                    },
                    colors = switchColors
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = ItirafTheme.colors.dividerColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            state.notificationItems.forEach { item ->
                NotificationRow(
                    item = item,
                    isMasterEnabled = state.isMasterEnabled,
                    onSwitchChanged = { isChecked ->
                        onEvent(NotificationEvent.OnItemSwitchChanged(item.type, isChecked))
                    }
                )
            }
        }
    }
}