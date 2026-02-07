package com.itirafapp.android.presentation.screens.message.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Report
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.itirafapp.android.R
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ChatActions(
    onBlockUserClick: () -> Unit,
    onReportUserClick: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { isMenuExpanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = "More",
                tint = ItirafTheme.colors.textPrimary
            )
        }

        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
            containerColor = ItirafTheme.colors.backgroundCard
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.block_user_title),
                        color = ItirafTheme.colors.statusError,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Block,
                        contentDescription = null,
                        tint = ItirafTheme.colors.statusError,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = {
                    isMenuExpanded = false
                    onBlockUserClick()
                }
            )

            HorizontalDivider(
                color = ItirafTheme.colors.dividerColor,
                thickness = 1.dp
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.report),
                        color = ItirafTheme.colors.statusError,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Report,
                        contentDescription = null,
                        tint = ItirafTheme.colors.statusError,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = {
                    isMenuExpanded = false
                    onReportUserClick()
                }
            )
        }
    }
}