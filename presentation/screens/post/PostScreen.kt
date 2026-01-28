package com.itirafapp.android.presentation.screens.post

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.core.ItirafButton
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun PostScreen(
    channelId: Int,
    onDismiss: () -> Unit,
    viewModel: PostViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = channelId) {
        val safeId = if (channelId == -1) null else channelId
        viewModel.onEvent(PostEvent.Init(safeId))
    }

    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is PostUiEvent.Dismiss -> {
                    onDismiss()
                }

                is PostUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    PostContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostContent(
    state: PostState,
    onEvent: (PostEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val maxTitleLength = 100
    val maxMessageLength = 500

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.post_navigation_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = ItirafTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(color = ItirafTheme.colors.dividerColor)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.post_title),
                    style = MaterialTheme.typography.labelLarge,
                    color = ItirafTheme.colors.textPrimary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "(${stringResource(R.string.post_options)})",
                    style = MaterialTheme.typography.bodySmall,
                    color = ItirafTheme.colors.textTertiary,
                    fontSize = 10.sp
                )
            }


            Text(
                text = stringResource(R.string.remaining, maxTitleLength - state.title.length),
                style = MaterialTheme.typography.bodySmall,
                color = ItirafTheme.colors.textSecondary,
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.title,
            onValueChange = { if (it.length <= maxTitleLength) onEvent(PostEvent.TitleChanged(it)) },
            placeholder = {
                Text(
                    text = stringResource(R.string.post_title_placeholder),
                    color = ItirafTheme.colors.textSecondary.copy(alpha = 0.5f)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ItirafTheme.colors.brandPrimary,
                unfocusedBorderColor = ItirafTheme.colors.dividerColor,
                cursorColor = ItirafTheme.colors.brandPrimary,
                focusedTextColor = ItirafTheme.colors.textPrimary,
                unfocusedTextColor = ItirafTheme.colors.textPrimary
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.post_description),
                style = MaterialTheme.typography.labelLarge,
                color = ItirafTheme.colors.textPrimary,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = stringResource(R.string.remaining, maxMessageLength - state.message.length),
                style = MaterialTheme.typography.bodySmall,
                color = ItirafTheme.colors.textSecondary,
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.message,
            onValueChange = { if (it.length <= maxMessageLength) onEvent(PostEvent.MessageChanged(it)) },
            placeholder = {
                Text(
                    text = stringResource(R.string.post_description_placeholder),
                    color = ItirafTheme.colors.textSecondary.copy(alpha = 0.5f)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ItirafTheme.colors.brandPrimary,
                unfocusedBorderColor = ItirafTheme.colors.dividerColor,
                cursorColor = ItirafTheme.colors.brandPrimary,
                focusedTextColor = ItirafTheme.colors.textPrimary,
                unfocusedTextColor = ItirafTheme.colors.textPrimary
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.post_channel),
            style = MaterialTheme.typography.labelLarge,
            color = ItirafTheme.colors.textPrimary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (!state.isChannelLocked) {
                    expanded = !expanded
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.selectedChannel?.title
                    ?: stringResource(R.string.post_select_channel),
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = !state.isChannelLocked
                    ),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = "Select Channel",
                        tint =
                            if (state.isChannelLocked) ItirafTheme.colors.textSecondary.copy(alpha = 0.4f)
                            else ItirafTheme.colors.textSecondary
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ItirafTheme.colors.brandPrimary,
                    unfocusedBorderColor = ItirafTheme.colors.dividerColor,
                    focusedTextColor = ItirafTheme.colors.textPrimary,
                    unfocusedTextColor = ItirafTheme.colors.textPrimary,
                    cursorColor = ItirafTheme.colors.brandPrimary,

                    disabledTextColor = ItirafTheme.colors.textSecondary.copy(alpha = 0.5f),
                    disabledBorderColor = ItirafTheme.colors.dividerColor.copy(alpha = 0.4f),
                    disabledTrailingIconColor = ItirafTheme.colors.textSecondary.copy(alpha = 0.4f),
                    disabledContainerColor = Color.Transparent,
                    disabledPlaceholderColor = ItirafTheme.colors.textSecondary.copy(alpha = 0.4f)
                ),
                enabled = !state.isChannelLocked
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(ItirafTheme.colors.backgroundCard)
                    .heightIn(max = 250.dp)
            ) {
                state.followedChannel.forEachIndexed { index, channel ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = channel.title,
                                color = ItirafTheme.colors.textPrimary
                            )
                        },
                        onClick = {
                            onEvent(PostEvent.ChannelSelected(channel))
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )

                    if (index < state.followedChannel.lastIndex) {
                        HorizontalDivider(
                            color = ItirafTheme.colors.dividerColor,
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        val isFormValid = state.message.isNotBlank() && state.selectedChannel != null

        ItirafButton(
            text = stringResource(R.string.share),
            onClick = { onEvent(PostEvent.SubmitClicked) },
            isLoading = state.isLoading,
            enabled = isFormValid,
            containerColor = ItirafTheme.colors.brandPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun PostScreenPreview() {
    ItirafAppTheme {
        PostContent(
            state = PostState(),
            onEvent = {}
        )
    }
}