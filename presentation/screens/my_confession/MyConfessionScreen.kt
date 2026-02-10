package com.itirafapp.android.presentation.screens.my_confession

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.MyConfessionData
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.my_confession.components.MyConfessionCard
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun MyConfessionScreen(
    onItemClick: (MyConfessionData) -> Unit,
    onEditClick: (MyConfessionData) -> Unit,
    viewModel: MyConfessionViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MyConfessionUiEvent.NavigateToDetail -> {
                    onItemClick(event.data)
                }

                is MyConfessionUiEvent.NavigateToEdit -> {
                    onEditClick(event.data)
                }

                is MyConfessionUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    MyConfessionContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyConfessionContent(
    state: MyConfessionState,
    onEvent: (MyConfessionEvent) -> Unit
) {
    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.my_confessions),
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onEvent(MyConfessionEvent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            if (!state.isLoading && state.myConfession.isEmpty() && state.error == null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Henüz hiç itirafın yok.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ItirafTheme.colors.textSecondary
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(state.myConfession) { index, confession ->
                    if (index >= state.myConfession.lastIndex && !state.isLoading) {
                        LaunchedEffect(Unit) {
                            onEvent(MyConfessionEvent.LoadMore)
                        }
                    }

                    MyConfessionCard(
                        confession = confession,
                        onCardClick = { onEvent(MyConfessionEvent.ItemClicked(confession.id)) },
                        onEditClick = { onEvent(MyConfessionEvent.EditClicked(confession.id)) }
                    )
                }

                if (state.isLoading && state.myConfession.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = ItirafTheme.colors.brandPrimary,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }

            if (state.isLoading && state.myConfession.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = ItirafTheme.colors.brandPrimary
                )
            }
        }
    }
}