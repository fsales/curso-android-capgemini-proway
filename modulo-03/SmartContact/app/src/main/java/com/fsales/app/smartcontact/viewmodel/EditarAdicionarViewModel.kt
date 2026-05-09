package com.fsales.app.smartcontact.viewmodel

import androidx.lifecycle.ViewModel
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.EditarAdicionarUiState
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.EditarAdicionarValidator
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.EditarAdicionarValidator.hasErrors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditarAdicionarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EditarAdicionarUiState())
    val uiState: StateFlow<EditarAdicionarUiState> = _uiState.asStateFlow()

    fun onNomeChange(value: String)       = _uiState.update { it.copy(nome = value,       errors = it.errors.copy(nome = null)) }
    fun onEmailChange(value: String)      = _uiState.update { it.copy(email = value,      errors = it.errors.copy(email = null)) }
    fun onTelefoneChange(value: String)   = _uiState.update { it.copy(telefone = value,   errors = it.errors.copy(telefone = null)) }
    fun onCepChange(value: String)        = _uiState.update { it.copy(cep = value,        errors = it.errors.copy(cep = null)) }
    fun onBairroChange(value: String)     = _uiState.update { it.copy(bairro = value,     errors = it.errors.copy(bairro = null)) }
    fun onLogradouroChange(value: String) = _uiState.update { it.copy(logradouro = value, errors = it.errors.copy(logradouro = null)) }
    fun onNumeroChange(value: String)     = _uiState.update { it.copy(numero = value,     errors = it.errors.copy(numero = null)) }
    fun onEstadoChange(value: String)     = _uiState.update { it.copy(estado = value,     errors = it.errors.copy(estado = null)) }
    fun onCidadeChange(value: String)     = _uiState.update { it.copy(cidade = value,     errors = it.errors.copy(cidade = null)) }

    fun onSalvar(): Boolean {
        val errors = EditarAdicionarValidator.validar(_uiState.value)
        _uiState.update { it.copy(errors = errors) }
        return !errors.hasErrors()
    }
}
