package com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.itirafapp.android.R
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.ModerationDetailEvent
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.ModerationDetailState
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.extension.getLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RejectFormContent(
    state: ModerationDetailState,
    onEvent: (ModerationDetailEvent) -> Unit
) {
    val context = LocalContext.current

    ExposedDropdownMenuBox(
        expanded = state.isViolationDropdownExpanded,
        onExpandedChange = { onEvent(ModerationDetailEvent.ToggleViolationDropdown) },
        modifier = Modifier.fillMaxWidth()
    ) {
        val selectedText = if (state.selectedViolations.isEmpty()) {
            stringResource(R.string.moderation_detail_violation_title)
        } else {
            state.selectedViolations.joinToString(", ") { it.getLabel(context) }
        }

        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            maxLines = 1,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isViolationDropdownExpanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ItirafTheme.colors.brandPrimary,
                cursorColor = ItirafTheme.colors.textPrimary
            ),
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = state.isViolationDropdownExpanded,
            onDismissRequest = { onEvent(ModerationDetailEvent.DismissViolationDropdown) }
        ) {
            state.availableViolations.forEach { violation ->
                val isSelected = state.selectedViolations.contains(violation)

                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = violation.getLabel(context),
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = null,
                                colors = CheckboxDefaults.colors(
                                    checkedColor = ItirafTheme.colors.brandPrimary,
                                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    },
                    onClick = {
                        onEvent(ModerationDetailEvent.ToggleViolation(violation))
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = stringResource(R.string.moderation_detail_rejection_title),
        style = MaterialTheme.typography.titleMedium,
        color = ItirafTheme.colors.textPrimary
    )
    Spacer(modifier = Modifier.height(4.dp))

    OutlinedTextField(
        value = state.rejectionReason,
        onValueChange = { onEvent(ModerationDetailEvent.EnteredRejectionReason(it)) },
        modifier = Modifier.fillMaxWidth(),
        minLines = 2,
        placeholder = { Text(stringResource(R.string.moderation_detail_rejection_placeholder)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ItirafTheme.colors.brandPrimary,
            cursorColor = ItirafTheme.colors.textPrimary
        )
    )
}