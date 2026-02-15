package com.itirafapp.android.presentation.screens.moderation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.domain.model.enums.ModerationFilter
import com.itirafapp.android.domain.model.enums.ModerationStatus
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

    private val allFetchedData = mutableListOf<ModerationData>()

    private var currentPage = 1
    private var isLastPage = false

    fun onEvent(event: ModerationEvent) {
        when (event) {
            is ModerationEvent.Refresh -> {
                currentPage = 1
                isLastPage = false
                loadModeration(isPullToRefresh = true)
            }

            is ModerationEvent.LoadData -> {
                loadModeration(isPullToRefresh = false)
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

            is ModerationEvent.FilterChanged -> {
                if (state.selectedFilter == event.filter) return
                state = state.copy(selectedFilter = event.filter)

                applyFilterToState()
            }
        }
    }

    private fun loadModeration(isPullToRefresh: Boolean = false) {

        getPendingModerationRequests(page = currentPage)
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        val shouldShowFullscreenLoading =
                            state.moderationData.isEmpty() && !isPullToRefresh

                        state = state.copy(
                            isLoading = shouldShowFullscreenLoading,
                            isRefreshing = isPullToRefresh,
                            error = null
                        )
                    }

                    is Resource.Success -> {
                        result.data.let { paginatedResult ->
                            isLastPage = !paginatedResult.hasNextPage

                            if (currentPage == 1) {
                                allFetchedData.clear()
                            }

                            allFetchedData.addAll(paginatedResult.items)

                            if (paginatedResult.hasNextPage) {
                                currentPage++
                            }

                            state = state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = null
                            )

                            applyFilterToState()
                        }
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.error.message
                        )
                        sendUiEvent(ModerationUiEvent.ShowMessage(result.error.message))
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun applyFilterToState() {
        val filteredList = when (state.selectedFilter) {
            ModerationFilter.ALL -> allFetchedData
            ModerationFilter.PENDING -> allFetchedData.filter {
                it.moderationStatus == ModerationStatus.PENDING_REVIEW ||
                        it.moderationStatus == ModerationStatus.NEEDS_HUMAN_REVIEW
            }

            ModerationFilter.REJECTED -> allFetchedData.filter {
                it.moderationStatus == ModerationStatus.HUMAN_REJECTED ||
                        it.moderationStatus == ModerationStatus.AI_REJECTED
            }
        }
        state = state.copy(moderationData = filteredList)
    }

    private fun sendUiEvent(event: ModerationUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}