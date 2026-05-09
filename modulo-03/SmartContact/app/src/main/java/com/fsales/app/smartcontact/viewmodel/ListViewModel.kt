package com.fsales.app.smartcontact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.smartcontact.repository.ContatoRepository
import com.fsales.app.smartcontact.ui.feature.lista.ListEvent
import com.fsales.app.smartcontact.ui.feature.lista.ListUiEvent
import com.fsales.app.smartcontact.ui.feature.lista.state.ListUiState
import com.fsales.app.smartcontact.ui.presentation.error.logTechnicalError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val contatoRepository: ContatoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState(carregando = true))
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ListUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            runCatching {
                contatoRepository.observeContatos().collect { contatos ->
                    _uiState.update { it.copy(contatos = contatos, carregando = false, erro = null) }
                }
            }.onFailure { throwable ->
                logTechnicalError(action = "carregar lista de contatos", throwable = throwable)
                _uiState.update { it.copy(carregando = false, erro = "erro_lista") }
            }
        }
    }

    fun onEvent(event: ListEvent) {
        when (event) {
            ListEvent.NavegaParaNovo -> viewModelScope.launch { _uiEvent.send(ListUiEvent.NavegaParaNovo) }
            is ListEvent.NavegaParaEdicao -> viewModelScope.launch { _uiEvent.send(ListUiEvent.NavegaParaEdicao(event.id)) }
            is ListEvent.Excluir -> excluir(event.id)
        }
    }

    private fun excluir(id: Long) {
        viewModelScope.launch {
            runCatching {
                contatoRepository.deleteContatoById(id)
            }.onSuccess {
                _uiEvent.send(ListUiEvent.ExcluirSucesso)
            }.onFailure { throwable ->
                logTechnicalError(action = "excluir contato", throwable = throwable)
                _uiEvent.send(ListUiEvent.ErroAoExcluir)
            }
        }
    }
}
