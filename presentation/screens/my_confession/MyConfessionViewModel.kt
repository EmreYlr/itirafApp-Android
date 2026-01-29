package com.itirafapp.android.presentation.screens.my_confession

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.user.GetMyConfessionsUseCase
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyConfessionViewModel @Inject constructor(
    private val getMyConfessionsUseCase: GetMyConfessionsUseCase
) : ViewModel() {

    var state by mutableStateOf(MyConfessionState())
        private set

    private val _uiEvent = Channel<MyConfessionUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var currentPage = 1
    private var isLastPage = false

    init {
        loadConfessions()
    }

    fun onEvent(event: MyConfessionEvent) {
        when (event) {
            is MyConfessionEvent.Refresh -> {
                currentPage = 1
                isLastPage = false
                loadConfessions(isPullToRefresh = true)
            }

            is MyConfessionEvent.LoadMore -> {
                if (!state.isLoading && !isLastPage) {
                    loadConfessions()
                }
            }

            is MyConfessionEvent.ItemClicked -> {
                val selectedItem = state.myConfession.find { it.id == event.id }
                selectedItem?.let {
                    sendUiEvent(MyConfessionUiEvent.NavigateToDetail(it))
                }
            }
        }
    }

    private fun loadConfessions(isPullToRefresh: Boolean = false) {
        getMyConfessionsUseCase(page = currentPage)
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
                        result.data?.let { paginatedResult ->
                            isLastPage = !paginatedResult.hasNextPage

                            val newItems = paginatedResult.items

                            val currentList = if (currentPage == 1) {
                                emptyList()
                            } else {
                                state.myConfession
                            }

                            state = state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                myConfession = currentList + newItems
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
                            error = result.message
                        )
                        sendUiEvent(
                            MyConfessionUiEvent.ShowMessage(
                                result.message ?: "Bir hata oluştu"
                            )
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: MyConfessionUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}