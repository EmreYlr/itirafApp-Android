package com.itirafapp.android.presentation.screens.moderation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.moderation.GetPendingModerationRequests
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModerationViewModel @Inject constructor(
    private val getPendingModerationRequests: GetPendingModerationRequests
) : ViewModel() {
    var state by mutableStateOf(ModerationState())
        private set

    private val _uiEvent = Channel<ModerationUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var currentPage = 1
    private var isLastPage = false

    init {
        loadModeration()
    }

    fun onEvent(event: ModerationEvent) {
        when (event) {
            is ModerationEvent.Refresh -> {
                currentPage = 1
                isLastPage = false
                loadModeration(isPullToRefresh = true)
            }

            is ModerationEvent.LoadMore -> {
                if (!state.isLoading && !isLastPage) {
                    loadModeration()
                }
            }

            is ModerationEvent.ItemClicked -> {
                val selectedItem = state.moderationData.find { it.id == event.id }
                selectedItem?.let {
                    sendUiEvent(ModerationUiEvent.NavigateToDetail(it))
                }
            }
        }
    }

    private fun loadModeration(isPullToRefresh: Boolean = false) {
        getPendingModerationRequests(page = currentPage)
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(
                            isLoading = !isPullToRefresh,
                            isRefreshing = isPullToRefresh,
                            error = null
                        )
                    }

                    is Resource.Success -> {
                        result.data.let { paginatedResult ->
                            isLastPage = !paginatedResult.hasNextPage

                            val newItems = paginatedResult.items

                            val currentList = if (currentPage == 1) {
                                emptyList()
                            } else {
                                state.moderationData
                            }

                            state = state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                moderationData = currentList + newItems
                            )

                            if (paginatedResult.hasNextPage) {
                                currentPage++
                            }
                        }
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.error.message
                        )
                        sendUiEvent(
                            ModerationUiEvent.ShowMessage(
                                result.error.message
                            )
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }


    private fun sendUiEvent(event: ModerationUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}