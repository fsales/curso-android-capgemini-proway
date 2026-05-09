package com.fsales.app.smartcontact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.smartcontact.ui.feature.lista.ListEvent
import com.fsales.app.smartcontact.ui.feature.lista.ListUiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {

    private val _uiEvent = Channel<ListUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ListEvent) {
        when (event) {
            ListEvent.NavegaParaNovo              -> viewModelScope.launch { _uiEvent.send(ListUiEvent.NavegaParaNovo) }
            is ListEvent.NavegaParaEdicao         -> viewModelScope.launch { _uiEvent.send(ListUiEvent.NavegaParaEdicao(event.id)) }
        }
    }
}

