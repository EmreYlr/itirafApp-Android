package com.itirafapp.android.presentation.screens.moderation.moderation_edit

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.screens.moderation.components.ModerationDecisionSegmentedControl
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.DecisionMode
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable.ApproveFormContent
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable.ModerationBottomBar
import com.itirafapp.android.presentation.screens.moderation.moderation_edit.components.EditRejectForm
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ModerationEditScreen(
    targetId: Int,
    isNsfw: Boolean,
    onDismiss: () -> Unit,
    viewModel: ModerationEditViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = targetId) {
        viewModel.onEvent(ModerationEditEvent.Init(targetId, isNsfw))
    }

    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ModerationEditUiEvent.Dismiss -> {
                    onDismiss()
                }

                is ModerationEditUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    ModerationEditContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModerationEditContent(
    state: ModerationEditState,
    onEvent: (ModerationEditEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
            .navigationBarsPadding()
            .imePadding(),
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.confession_edit),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = ItirafTheme.colors.textPrimary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ModerationDecisionSegmentedControl(
            selectedMode = state.decisionMode,
            onModeSelected = { onEvent(ModerationEditEvent.ChangeDecisionMode(it)) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state.decisionMode == DecisionMode.APPROVE) {
            ApproveFormContent(
                isNsfw = state.isNsfw,
                onToggleNsfw = { onEvent(ModerationEditEvent.ToggleNsfw(it)) }
            )
        } else {
            EditRejectForm(
                state = state,
                onEvent = onEvent
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ModerationBottomBar(
            isLoading = state.isLoading,
            onSubmit = { onEvent(ModerationEditEvent.Submit) }
        )
    }
}