package com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.itirafapp.android.R
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ModeratorNoteField(
    note: String,
    onNoteChange: (String) -> Unit
) {
    Text(
        text = stringResource(R.string.moderation_detail_note_title),
        style = MaterialTheme.typography.titleMedium,
        color = ItirafTheme.colors.textPrimary
    )
    Spacer(modifier = Modifier.height(4.dp))

    OutlinedTextField(
        value = note,
        onValueChange = onNoteChange,
        modifier = Modifier.fillMaxWidth(),
        minLines = 2,
        placeholder = { Text(stringResource(R.string.moderation_detail_note_placeholder)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ItirafTheme.colors.brandPrimary,
            cursorColor = ItirafTheme.colors.textPrimary
        )
    )
}