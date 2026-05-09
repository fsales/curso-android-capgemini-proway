package com.fsales.app.smartcontact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.smartcontact.repository.CepException
import com.fsales.app.smartcontact.repository.CepRepository
import com.fsales.app.smartcontact.repository.ContatoRepository
import com.fsales.app.smartcontact.ui.feature.editaradicionar.EditarAdicionarEvent
import com.fsales.app.smartcontact.ui.feature.editaradicionar.EditarAdicionarUiEvent
import com.fsales.app.smartcontact.ui.feature.editaradicionar.mapper.toDomain
import com.fsales.app.smartcontact.ui.feature.editaradicionar.mapper.toUiState
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.EditarAdicionarUiState
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.EditarAdicionarValidator
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.EditarAdicionarValidator.hasErrors
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.FieldError
import com.fsales.app.smartcontact.ui.presentation.error.logTechnicalError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val CEP_DEBOUNCE_MS = 300L
private const val CEP_LENGTH = 8

@HiltViewModel
class EditarAdicionarViewModel @Inject constructor(
    private val contatoRepository: ContatoRepository,
    private val cepRepository: CepRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditarAdicionarUiState())
    val uiState: StateFlow<EditarAdicionarUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<EditarAdicionarUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private var jaSubmeteu = false

    /** Job cancelável para o debounce de busca por CEP. */
    private var cepJob: Job? = null

    fun carregarContato(id: Long?) {
        if (id == null || id == 0L) return

        viewModelScope.launch {
            runCatching {
                contatoRepository.getContatoById(id)
            }.onSuccess { contato ->
                if (contato != null) {
                    _uiState.value = contato.toUiState()
                } else {
                    logTechnicalError(
                        action = "carregar contato",
                        throwable = IllegalStateException("Contato nao encontrado: id=$id"),
                    )
                }
            }.onFailure { throwable ->
                logTechnicalError(action = "carregar contato", throwable = throwable)
            }
        }
    }

    fun onEvent(event: EditarAdicionarEvent) {
        when (event) {
            is EditarAdicionarEvent.AlterarNome -> _uiState.update { it.copy(nome = event.valor, errors = it.errors.copy(nome = null)) }
            is EditarAdicionarEvent.AlterarEmail -> _uiState.update { it.copy(email = event.valor, errors = it.errors.copy(email = null)) }
            is EditarAdicionarEvent.AlterarTelefone -> _uiState.update { it.copy(telefone = event.valor, errors = it.errors.copy(telefone = null)) }
            is EditarAdicionarEvent.AlterarDataNascimento -> _uiState.update { it.copy(dataNascimento = event.data, errors = it.errors.copy(dataNascimento = null)) }
            is EditarAdicionarEvent.AlterarCep -> onAlterarCep(event.valor)
            is EditarAdicionarEvent.AlterarBairro -> _uiState.update { it.copy(bairro = event.valor, errors = it.errors.copy(bairro = null)) }
            is EditarAdicionarEvent.AlterarLogradouro -> _uiState.update { it.copy(logradouro = event.valor, errors = it.errors.copy(logradouro = null)) }
            is EditarAdicionarEvent.AlterarNumero -> _uiState.update { it.copy(numero = event.valor, errors = it.errors.copy(numero = null)) }
            is EditarAdicionarEvent.AlterarEstado -> { /* somente leitura — preenchido via ViaCEP */ }
            is EditarAdicionarEvent.AlterarCidade -> { /* somente leitura — preenchido via ViaCEP */ }
            EditarAdicionarEvent.Salvar -> salvar()
            EditarAdicionarEvent.Voltar -> {
                resetar()
                viewModelScope.launch { _uiEvent.send(EditarAdicionarUiEvent.NavigateBack) }
            }
        }
    }

    // -------------------------------------------------------------------------
    // CEP — debounce + busca ViaCEP
    // -------------------------------------------------------------------------

    private fun onAlterarCep(cep: String) {
        // Atualiza o campo imediatamente e limpa erro anterior
        _uiState.update { it.copy(cep = cep, errors = it.errors.copy(cep = null)) }

        // Cancela busca anterior (enquanto o usuário ainda digita)
        cepJob?.cancel()

        if (cep.length == CEP_LENGTH) {
            cepJob = viewModelScope.launch {
                delay(CEP_DEBOUNCE_MS)
                buscarCep(cep)
            }
        }
    }

    private suspend fun buscarCep(cep: String) {
        _uiState.update { it.copy(carregandoCep = true) }

        cepRepository.buscarCep(cep)
            .onSuccess { resposta ->
                _uiState.update { state ->
                    state.copy(
                        carregandoCep = false,
                        // Somente sobrescreve se a API retornou valor não-vazio;
                        // caso contrário, preserva o que o usuário digitou.
                        logradouro = resposta.logradouro.ifEmpty { state.logradouro },
                        bairro     = resposta.bairro.ifEmpty { state.bairro },
                        // Cidade e estado são sempre preenchidos pela API (somente leitura)
                        cidade = resposta.localidade,
                        estado = resposta.estado,
                        errors = state.errors.copy(cep = null),
                    )
                }
            }
            .onFailure { throwable ->
                val fieldError = when (throwable) {
                    is CepException.CepNaoEncontrado -> FieldError.CepNaoEncontrado
                    else -> {
                        logTechnicalError(action = "buscar cep", throwable = throwable)
                        FieldError.CepNaoEncontrado
                    }
                }
                _uiState.update { it.copy(carregandoCep = false, errors = it.errors.copy(cep = fieldError)) }
            }
    }

    // -------------------------------------------------------------------------
    // Salvar / Resetar
    // -------------------------------------------------------------------------

    private fun salvar() {
        jaSubmeteu = true
        val errors = EditarAdicionarValidator.validar(_uiState.value)
        _uiState.update { it.copy(errors = errors) }

        if (!errors.hasErrors()) {
            viewModelScope.launch {
                runCatching {
                    contatoRepository.saveContato(_uiState.value.toDomain())
                }.onSuccess {
                    resetar()
                    _uiEvent.send(EditarAdicionarUiEvent.NavigateBack)
                }.onFailure { throwable ->
                    logTechnicalError(action = "salvar contato", throwable = throwable)
                    _uiEvent.send(EditarAdicionarUiEvent.ErroAoSalvar)
                }
            }
        }
    }

    private fun resetar() {
        jaSubmeteu = false
        cepJob?.cancel()
        cepJob = null  // Explícito — garante que job anterior não pode ser reutilizado
        _uiState.value = EditarAdicionarUiState()
    }
}
