package com.itirafapp.android.presentation.screens.my_confession.my_confession_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.model.MyConfessionData
import com.itirafapp.android.domain.usecase.confession.DeleteConfessionUseCase
import com.itirafapp.android.domain.usecase.confession.EditConfessionUseCase
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyConfessionEditViewModel @Inject constructor(
    private val editConfessionUseCase: EditConfessionUseCase,
    private val deleteConfessionUseCase: DeleteConfessionUseCase
) : ViewModel() {

    var state by mutableStateOf(MyConfessionEditState())
        private set

    private val _uiEvent = Channel<MyConfessionEditUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun setInitialData(data: MyConfessionData) {
        if (state.confessions == null) {
            state = state.copy(
                confessions = data, title = data.title ?: "", message = data.message
            )
        }
    }

    fun onEvent(event: MyConfessionEditEvent) {
        when (event) {
            is MyConfessionEditEvent.BackClicked -> {
                sendUiEvent(MyConfessionEditUiEvent.NavigateToBack)
            }

            is MyConfessionEditEvent.TitleTextChanged -> {
                state = state.copy(title = event.text)
            }

            is MyConfessionEditEvent.MessageTextChanged -> {
                state = state.copy(message = event.text)
            }

            is MyConfessionEditEvent.DeleteIconClicked -> {
                state = state.copy(showDeleteDialog = true)
            }

            is MyConfessionEditEvent.DeleteDialogDismissed -> {
                state = state.copy(showDeleteDialog = false)
            }

            is MyConfessionEditEvent.DeleteConfirmed -> {
                deleteConfession()
            }

            is MyConfessionEditEvent.SendClicked -> {
                updateConfession()
            }
        }
    }

    private fun updateConfession() {
        val currentConfession = state.confessions ?: return
        val title = state.title?.trim()
        val message = state.message.trim()

        if (message.isBlank()) {
            sendUiEvent(MyConfessionEditUiEvent.ShowMessage("İtiraf metni boş olamaz."))
            return
        }

        editConfessionUseCase(
            id = currentConfession.id, title = title, message = message
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true, error = null)
                }

                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(MyConfessionEditUiEvent.ShowMessage("İtiraf başarıyla güncellendi."))
                    sendUiEvent(MyConfessionEditUiEvent.NavigateToBack)
                }

                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false, error = result.message
                    )
                    sendUiEvent(
                        MyConfessionEditUiEvent.ShowMessage(
                            result.message ?: "Güncelleme başarısız."
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun deleteConfession() {
        val id = state.confessions?.id ?: return

        deleteConfessionUseCase(id).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true, error = null)
                }

                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(MyConfessionEditUiEvent.ShowMessage("İtiraf silindi."))
                    sendUiEvent(MyConfessionEditUiEvent.NavigateToBack)
                }

                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false, error = result.message
                    )
                    sendUiEvent(
                        MyConfessionEditUiEvent.ShowMessage(
                            result.message ?: "Silme işlemi başarısız."
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: MyConfessionEditUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}