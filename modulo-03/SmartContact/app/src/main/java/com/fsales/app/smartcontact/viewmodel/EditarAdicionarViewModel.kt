package com.fsales.app.smartcontact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.smartcontact.ui.feature.editaradicionar.EditarAdicionarEvent
import com.fsales.app.smartcontact.ui.feature.editaradicionar.EditarAdicionarUiEvent
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.EditarAdicionarUiState
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.EditarAdicionarValidator
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.EditarAdicionarValidator.hasErrors
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditarAdicionarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EditarAdicionarUiState())
    val uiState: StateFlow<EditarAdicionarUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<EditarAdicionarUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private var jaSubmeteu = false

    fun onEvent(event: EditarAdicionarEvent) {
        when (event) {
            is EditarAdicionarEvent.AlterarNome         -> _uiState.update { it.copy(nome = event.valor,       errors = it.errors.copy(nome = null)) }
            is EditarAdicionarEvent.AlterarEmail        -> _uiState.update { it.copy(email = event.valor,      errors = it.errors.copy(email = null)) }
            is EditarAdicionarEvent.AlterarTelefone     -> _uiState.update { it.copy(telefone = event.valor,   errors = it.errors.copy(telefone = null)) }
            is EditarAdicionarEvent.AlterarDataNascimento -> _uiState.update { it.copy(dataNascimento = event.data, errors = it.errors.copy(dataNascimento = null)) }
            is EditarAdicionarEvent.AlterarCep          -> _uiState.update { it.copy(cep = event.valor,        errors = it.errors.copy(cep = null)) }
            is EditarAdicionarEvent.AlterarBairro       -> _uiState.update { it.copy(bairro = event.valor,     errors = it.errors.copy(bairro = null)) }
            is EditarAdicionarEvent.AlterarLogradouro   -> _uiState.update { it.copy(logradouro = event.valor, errors = it.errors.copy(logradouro = null)) }
            is EditarAdicionarEvent.AlterarNumero       -> _uiState.update { it.copy(numero = event.valor,     errors = it.errors.copy(numero = null)) }
            is EditarAdicionarEvent.AlterarEstado       -> _uiState.update { it.copy(estado = event.valor,     errors = it.errors.copy(estado = null)) }
            is EditarAdicionarEvent.AlterarCidade       -> _uiState.update { it.copy(cidade = event.valor,     errors = it.errors.copy(cidade = null)) }
            EditarAdicionarEvent.Salvar                 -> salvar()
            EditarAdicionarEvent.Voltar                 -> {
                resetar()
                viewModelScope.launch { _uiEvent.send(EditarAdicionarUiEvent.NavigateBack) }
            }
        }
    }

    private fun salvar() {
        jaSubmeteu = true
        val errors = EditarAdicionarValidator.validar(_uiState.value)
        _uiState.update { it.copy(errors = errors) }
        if (!errors.hasErrors()) {
            viewModelScope.launch {
                runCatching {
                    // TODO: chamar UseCase/Repository para salvar
                }.onSuccess {
                    resetar()
                    _uiEvent.send(EditarAdicionarUiEvent.NavigateBack)
                }.onFailure {
                    _uiEvent.send(EditarAdicionarUiEvent.ErroAoSalvar)
                }
            }
        }
    }

    private fun resetar() {
        jaSubmeteu = false
        _uiState.value = EditarAdicionarUiState()
    }
}
