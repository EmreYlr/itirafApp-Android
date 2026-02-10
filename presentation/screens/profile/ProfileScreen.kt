package com.itirafapp.android.presentation.screens.profile

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.Link
import com.itirafapp.android.presentation.components.core.EmptyStateView
import com.itirafapp.android.presentation.components.core.ItirafButton
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.profile.components.SocialCard
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToFollowChannel: () -> Unit,
    onNavigateToSocial: (Link?) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.initializeProfile()
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ProfileUiEvent.NavigateToSettings -> {
                    onNavigateToSettings()
                }

                is ProfileUiEvent.NavigateToFollowChannel -> {
                    onNavigateToFollowChannel()
                }

                is ProfileUiEvent.NavigateToSocial -> {
                    onNavigateToSocial(event.link)
                }

                is ProfileUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    ProfileContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit
) {
    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.profile_title),
                actions = {
                    IconButton(
                        onClick = { onEvent(ProfileEvent.SettingsClicked) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = ItirafTheme.colors.textSecondary,
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onEvent(ProfileEvent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(
                                color = ItirafTheme.colors.backgroundCard,
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dp,
                                color = ItirafTheme.colors.dividerColor,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_profile),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = state.user?.username ?: "Anonymous",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = ItirafTheme.colors.textPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { onEvent(ProfileEvent.FollowChannelClicked) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ItirafTheme.colors.backgroundCard,
                                contentColor = ItirafTheme.colors.textPrimary
                            ),
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                            modifier = Modifier.height(30.dp),
                            enabled = !state.isAnonymous
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Group,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = ItirafTheme.colors.brandPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.followed_channels_title),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                HorizontalDivider(
                    color = ItirafTheme.colors.dividerColor,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = ItirafTheme.colors.brandPrimary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.profile_social_info_title),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = ItirafTheme.colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.profile_social_info_body),
                            style = MaterialTheme.typography.bodySmall,
                            color = ItirafTheme.colors.textSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                ItirafButton(
                    text = stringResource(R.string.add_new_social_link),
                    onClick = { onEvent(ProfileEvent.AddSocialClick) },
                    containerColor = ItirafTheme.colors.brandSecondary,
                    contentColor = Color.White,
                    icon = Icons.Default.Add,
                    enabled = !state.isAnonymous
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    state.user?.socialLinks?.forEach { link ->
                        SocialCard(
                            social = link,
                            onEditClick = { selectedLink ->
                                onEvent(ProfileEvent.EditSocialClick(selectedLink.id))
                            },
                            onVisibilityChange = { isVisible ->
                                onEvent(ProfileEvent.SocialVisibilityChanged(link.id, isVisible))
                            }
                        )
                    }
                }
                if (!state.isAnonymous && state.user?.socialLinks.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyStateView(
                            icon = Icons.Default.Link,
                            message = stringResource(R.string.empty_noSocialMediaLinks_title)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun ProfileScreenPreview() {
    ItirafAppTheme {
        ProfileContent(
            state = ProfileState(),
            onEvent = {}
        )
    }
}