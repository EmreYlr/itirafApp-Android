package com.itirafapp.android.presentation.screens.terms

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.core.ItirafButton
import com.itirafapp.android.presentation.model.TermsItem
import com.itirafapp.android.presentation.screens.terms.components.TermRowItem
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.extension.openUrlSafe
import com.itirafapp.android.util.state.UiText

@Composable
fun TermsScreen(
    onTermsComplete: () -> Unit,
    onAuthRedirect: () -> Unit,
    viewModel: TermsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val colorParams = ItirafTheme.colors.brandPrimary.toArgb()

    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is TermsUiEvent.NavigateToHome -> {
                    onTermsComplete()
                }

                is TermsUiEvent.NavigateToAuth -> {
                    onAuthRedirect()
                }

                is TermsUiEvent.NavigateToUrl -> {
                    openUrlSafe(context, event.url, colorParams)
                }
            }
        }
    }

    TermsContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TermsContent(
    state: TermsState,
    onEvent: (TermsEvent) -> Unit
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .systemBarsPadding()
    ) {
        Text(
            text = stringResource(R.string.terms_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = ItirafTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            state.items.forEach { item ->
                TermRowItem(item = item)
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column {
            Surface(
                onClick = { onEvent(TermsEvent.OnPrivacyPolicyClick) },
                shape = RoundedCornerShape(8.dp),
                color = ItirafTheme.colors.backgroundCard,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.terms_detail_button),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = ItirafTheme.colors.brandPrimary
                    )
                    Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        tint = ItirafTheme.colors.textSecondary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.terms_accept_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = ItirafTheme.colors.textPrimary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                Switch(
                    checked = state.isAccepted,
                    onCheckedChange = { isChecked ->
                        onEvent(TermsEvent.OnToggleAcceptance(isChecked))
                    },
                    colors = switchColors,
                    thumbContent = null
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ItirafButton(
                text = stringResource(R.string.terms_show_app_button),
                onClick = { onEvent(TermsEvent.OnStartClick) },
                enabled = state.isAccepted,
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TermsContentPreview_Default() {
    val mockItems = listOf(
        TermsItem(
            title = UiText.DynamicString("Gizlilik Politikası"),
            description = UiText.DynamicString("Verileriniz şifrelenir ve asla üçüncü şahıslarla paylaşılmaz."),
            icon = Icons.Rounded.PrivacyTip
        ),
        TermsItem(
            title = UiText.DynamicString("Topluluk Kuralları"),
            description = UiText.DynamicString("Saygı çerçevesinde, anonim olarak dertleşin."),
            icon = Icons.Rounded.Groups
        ),
        TermsItem(
            title = UiText.DynamicString("Güvenlik"),
            description = UiText.DynamicString("Rahatsız edici içerikleri anında bildirin."),
            icon = Icons.Rounded.Shield
        )
    )

    ItirafAppTheme {
        TermsContent(
            state = TermsState(
                items = mockItems,
                isAccepted = false
            ),
            onEvent = {}
        )
    }
}